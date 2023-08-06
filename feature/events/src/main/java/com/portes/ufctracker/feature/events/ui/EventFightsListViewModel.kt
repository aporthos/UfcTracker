package com.portes.ufctracker.feature.events.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portes.ufctracker.core.common.models.Result
import com.portes.ufctracker.core.data.repositories.FightersRepository
import com.portes.ufctracker.core.domain.usecase.AddOrRemoveFightBetsUseCase
import com.portes.ufctracker.core.domain.usecase.GetFightsListUseCase
import com.portes.ufctracker.core.domain.usecase.GetNicknameUseCase
import com.portes.ufctracker.core.domain.usecase.SaveNicknameUseCase
import com.portes.ufctracker.core.model.entities.EventRequest
import com.portes.ufctracker.core.model.models.FightModel
import com.portes.ufctracker.core.model.models.FighterModel
import com.portes.ufctracker.feature.events.ui.navigation.EventsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class EventsFightsListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getFightsListUseCase: GetFightsListUseCase,
    private val saveNicknameUseCase: SaveNicknameUseCase,
    private val getNicknameUseCase: GetNicknameUseCase,
    private val fightersRepository: FightersRepository,
    private val addOrRemoveFightBetsUseCase: AddOrRemoveFightBetsUseCase,
) : ViewModel() {

    private val eventsArgs: EventsArgs = EventsArgs(savedStateHandle)

    val eventId = eventsArgs.eventId
    val eventName = eventsArgs.name

    private val fighterBetsForAdd = hashMapOf<Int, FighterModel>()
    private val fighterBetsForRemove = hashMapOf<Int, FighterModel>()

    private var shouldShowAlertSaveNickname = MutableStateFlow(false)
    private var shouldShowButtonCreateFightBets =
        MutableStateFlow<Map<Int, FighterModel>?>(mapOf())
    private var shouldShowAlertSaveFightBets = MutableStateFlow<Boolean?>(null)
    private var fighterBetsLocal = MutableStateFlow<List<FightModel>>(emptyList())
    private var totalBets = -1

    private val showButtonCreateFightBets = MutableStateFlow(false)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            fightersRepository.getAndSaveFights(eventsArgs.eventId).collect {
                Timber.i("getAndSaveFights : $it")
            }
        }
    }

    val uiState: StateFlow<EventUiState> = combine(
        getFightsListUseCase(
            GetFightsListUseCase.Params(eventsArgs.eventId)
        ),
        fighterBetsLocal,
        shouldShowAlertSaveNickname,
        shouldShowAlertSaveFightBets,
        showButtonCreateFightBets,
    ) {
        result, fighterBetsLocal, showAlertSaveNickname, showAlertSaveFightBets, showButtonCreateFightBets,
        ->
        when (result) {
            Result.Loading -> EventUiState.Loading
            is Result.Success -> {
                EventUiState.Success(
                    SuccessEvents(
                        fights = result.data,
                        fightsBets = fighterBetsLocal,
                        shouldShowAlertSaveNickname = showAlertSaveNickname,
                        shouldShowAlertSaveFightBets = showAlertSaveFightBets,
                        shouldShowButtonCreateFightBets = showButtonCreateFightBets
                    )
                )
            }
            is Result.Error -> EventUiState.Error(
                error = result.exception
            )
        }
    }.stateIn(
        scope = viewModelScope,
        initialValue = EventUiState.Loading,
        started = SharingStarted.WhileSubscribed(),
    )

    fun createFightBet() {
        val nickname = getNicknameUseCase()
        if (nickname.isEmpty()) {
            shouldShowAlertSaveNickname.update { true }
            return
        }

        viewModelScope.launch {
            val params = AddOrRemoveFightBetsUseCase.Params(
                eventRequest = EventRequest(
                    eventName = eventsArgs.name,
                    day = eventsArgs.eventDate,
                    dayTime = eventsArgs.eventDateTime,
                    eventId = eventsArgs.eventId
                )
            )
            addOrRemoveFightBetsUseCase(params).collect { result ->
                when (result) {
                    Result.Loading -> Unit
                    is Result.Success -> {
                        Timber.i("createFightBet: Update success")
                    }
                    is Result.Error -> {
                        Timber.e(result.exception)
                    }
                }
            }
        }
    }

    private fun hasEmptyUpdateFights() =
        fighterBetsForAdd.isEmpty() && fighterBetsForRemove.isEmpty()

    fun onBackPressed() {
        if (hasEmptyUpdateFights()) {
            shouldShowAlertSaveFightBets.update { false }
        } else {
            if (totalBets == 0) {
                onlyDeleteFight()
                shouldShowAlertSaveFightBets.update { false }
            } else {
                shouldShowAlertSaveFightBets.update { true }
            }
        }
    }

    private fun onlyDeleteFight() = viewModelScope.launch {
        val params = AddOrRemoveFightBetsUseCase.Params(
            eventRequest = EventRequest(
                eventName = eventsArgs.name,
                day = eventsArgs.eventDate,
                dayTime = eventsArgs.eventDateTime,
                eventId = eventsArgs.eventId
            ),
        )
        addOrRemoveFightBetsUseCase(params).collect { result ->
            when (result) {
                Result.Loading -> Unit
                is Result.Success -> {
                    Timber.i("Deleted -> ${result.data}")
                }
                is Result.Error -> {
                    Timber.e(result.exception)
                }
            }
        }
    }

    fun shouldShowAlertSaveNickname(show: Boolean) {
        shouldShowAlertSaveNickname.update { show }
    }

    fun shouldShowAlertSaveFightBets(show: Boolean?) {
        shouldShowAlertSaveFightBets.update { show }
    }

    fun resetFighterBet() {
        fighterBetsLocal.update { emptyList() }
    }

    fun saveNicknameAndCreateFightBet(nickname: String) {
        saveNicknameUseCase(nickname)
        createFightBet()
    }

    fun addFighterToBet(isAddFighterBet: Boolean, fighter: FighterModel) =
        viewModelScope.launch {
            fightersRepository.updateFight(
                eventId = eventId,
                isSelectedBet = isAddFighterBet,
                fightId = fighter.fightId,
                fighterId = fighter.fighterId
            )
        }

    fun reset() {
        shouldShowButtonCreateFightBets.update { emptyMap() }
    }
}

sealed interface EventUiState {
    object Loading : EventUiState
    data class Success(
        var data: SuccessEvents
    ) : EventUiState

    data class Error(val error: String? = null) : EventUiState
}

data class SuccessEvents(
    var fights: List<FightModel>,
    val fightsBets: List<FightModel> = emptyList(),
    val shouldShowAlertSaveNickname: Boolean,
    val shouldShowAlertSaveFightBets: Boolean?,
    val shouldShowButtonCreateFightBets: Boolean,
)
