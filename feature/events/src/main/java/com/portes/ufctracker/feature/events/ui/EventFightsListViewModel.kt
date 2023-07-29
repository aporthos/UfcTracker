package com.portes.ufctracker.feature.events.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portes.ufctracker.core.common.models.Result
import com.portes.ufctracker.core.domain.usecase.AddOrRemoveFightBetsUseCase
import com.portes.ufctracker.core.domain.usecase.GetFightsListUseCase
import com.portes.ufctracker.core.domain.usecase.GetNicknameUseCase
import com.portes.ufctracker.core.domain.usecase.SaveNicknameUseCase
import com.portes.ufctracker.core.model.models.FightModel
import com.portes.ufctracker.core.model.models.FighterBetRequestModel
import com.portes.ufctracker.core.model.models.FighterModel
import com.portes.ufctracker.feature.events.ui.navigation.EventsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val addOrRemoveFightBetsUseCase: AddOrRemoveFightBetsUseCase,
) : ViewModel() {

    private val eventsArgs: EventsArgs = EventsArgs(savedStateHandle)

    val eventId = eventsArgs.eventId
    val eventName = eventsArgs.name

    private val fighterBetsForAdd = hashMapOf<Int, FighterModel>()
    private val fighterBetsForRemove = hashMapOf<Int, FighterModel>()

    private var shouldShowAlertDialog = MutableStateFlow(false)
    private var fighterBetsLocal = MutableStateFlow<List<FightModel>>(emptyList())
    private var fightsRemote = mutableListOf<FightModel>()

    val uiState: StateFlow<EventUiState> = combine(
        getFightsListUseCase(
            GetFightsListUseCase.Params(eventsArgs.eventId)
        ),
        fighterBetsLocal,
        shouldShowAlertDialog,
    ) { result, fighterBetsLocal, shouldShowAlertDialog ->
        when (result) {
            Result.Loading -> EventUiState.Loading
            is Result.Success -> {
                fightsRemote = result.data.toMutableList()
                EventUiState.Success(
                    SuccessEvents(
                        fights = result.data,
                        fightsBets = fighterBetsLocal,
                        shouldShowAlertDialog = shouldShowAlertDialog
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
            shouldShowAlertDialog.update { true }
            return
        }

        if (fighterBetsForAdd.isEmpty() && fighterBetsForRemove.isEmpty()) {
            fighterBetsLocal.update {
                getOnlySelectedBet()
            }
            return
        }

        viewModelScope.launch {
            val addFighterBets = fighterBetsForAdd.map {
                FighterBetRequestModel(it.key, it.value)
            }
            val removeFighterBets = fighterBetsForRemove.map {
                FighterBetRequestModel(it.key, it.value)
            }
            val params = AddOrRemoveFightBetsUseCase.Params(
                eventId = eventsArgs.eventId,
                eventName = eventsArgs.name,
                addFighterBets = addFighterBets,
                removeFighterBets = removeFighterBets,
            )
            addOrRemoveFightBetsUseCase(params).collect { result ->
                when (result) {
                    Result.Loading -> Unit
                    is Result.Success -> {
                        removeOrAddFight(addFighterBets, removeFighterBets)
                        fighterBetsLocal.update {
                            getOnlySelectedBet()
                        }
                        fighterBetsForAdd.clear()
                        fighterBetsForRemove.clear()
                    }
                    is Result.Error -> {
                        Timber.e(result.exception)
                    }
                }
            }
        }
    }

    private fun removeOrAddFight(
        addFighterBets: List<FighterBetRequestModel>,
        removeFighterBets: List<FighterBetRequestModel>
    ) {

        addFighterBets.map { fighter ->
            fightsRemote.first { it.fightId == fighter.fightId }.fighters.map {
                it.isSelectedBet = false
            }
        }

        addFighterBets.map { fighterBets ->
            fightsRemote.first { it.fightId == fighterBets.fightId }.fighters.first {
                it.fighterId == fighterBets.fighter.fighterId
            }.isSelectedBet = true
        }

        removeFighterBets.map { fighterBets ->
            fightsRemote.first { it.fightId == fighterBets.fightId }.fighters.first {
                it.fighterId == fighterBets.fighter.fighterId
            }.isSelectedBet = false
        }
    }

    private fun getOnlySelectedBet(): MutableList<FightModel> {
        val listFightBets = mutableListOf<FightModel>()
        fightsRemote.filter { fight ->
            fight.fighters.any { it.isSelectedBet }
        }.map {
            listFightBets.add(it)
        }
        return listFightBets
    }

    fun shouldShowAlertDialog(show: Boolean) {
        shouldShowAlertDialog.update { show }
    }

    fun resetFighterBet() {
        fighterBetsLocal.update { emptyList() }
    }

    fun saveNicknameAndCreateFightBet(nickname: String) {
        saveNicknameUseCase(nickname)
        createFightBet()
    }

    fun addFighterToBet(isAddFighterBet: Boolean, fightId: Int, fighter: FighterModel) {
        if (isAddFighterBet) {
            fighterBetsForAdd[fightId] = fighter
            fighterBetsForRemove.remove(fightId)
        } else {
            fighterBetsForAdd.remove(fightId)
            fighterBetsForRemove[fightId] = fighter
        }
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
    val shouldShowAlertDialog: Boolean
)
