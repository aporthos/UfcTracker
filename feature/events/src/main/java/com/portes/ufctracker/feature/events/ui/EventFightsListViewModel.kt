package com.portes.ufctracker.feature.events.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portes.ufctracker.core.common.models.Result
import com.portes.ufctracker.core.domain.usecase.AddOrRemoveFightBetsUseCase
import com.portes.ufctracker.core.domain.usecase.GetFightsListUseCase
import com.portes.ufctracker.core.domain.usecase.GetNicknameUseCase
import com.portes.ufctracker.core.domain.usecase.SaveNicknameUseCase
import com.portes.ufctracker.core.model.models.EventModel
import com.portes.ufctracker.core.model.models.FighterBetRequestModel
import com.portes.ufctracker.core.model.models.FighterModel
import com.portes.ufctracker.feature.events.ui.navigation.EventsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    private var _shouldShowAlertDialog = MutableStateFlow(false)
    val shouldShowAlertDialog: StateFlow<Boolean> = _shouldShowAlertDialog.asStateFlow()

    private val _showToast = MutableSharedFlow<String>()
    val showToast: SharedFlow<String> = _showToast.asSharedFlow()

    val uiState: StateFlow<EventUiState> = getFightsListUseCase(
        GetFightsListUseCase.Params(eventsArgs.eventId)
    ).map { resultList ->
        when (resultList) {
            Result.Loading -> EventUiState.Loading
            is Result.Success -> EventUiState.Success(resultList.data)
            is Result.Error -> EventUiState.Error(resultList.exception)
        }
    }.stateIn(
        scope = viewModelScope,
        initialValue = EventUiState.Loading,
        started = SharingStarted.WhileSubscribed(),
    )

    fun createFightBet() {
        val nickname = getNicknameUseCase()
        if (nickname.isEmpty()) {
            _shouldShowAlertDialog.tryEmit(true)
            return
        }

        viewModelScope.launch {
            val addFighterBetsAdd = fighterBetsForAdd.map {
                FighterBetRequestModel(it.key, it.value)
            }
            val removeFighterBets = fighterBetsForRemove.map {
                FighterBetRequestModel(it.key, it.value)
            }
            val params = AddOrRemoveFightBetsUseCase.Params(
                eventId = eventsArgs.eventId,
                eventName = eventsArgs.name,
                addFighterBets = addFighterBetsAdd,
                removeFighterBets = removeFighterBets,
            )
            addOrRemoveFightBetsUseCase(params).collect { result ->
                when (result) {
                    Result.Loading -> Timber.i("Result -> Loading")
                    is Result.Success -> {
                        _showToast.emit("Se actualizo tu apuesta")
                    }
                    is Result.Error -> {
                        Timber.e(result.exception)
                        _showToast.emit(result.exception.orEmpty())
                    }
                }
            }
        }
    }

    fun shouldShowAlertDialog(boolean: Boolean) {
        _shouldShowAlertDialog.value = boolean
    }

    fun saveNickname(nickname: String) {
        saveNicknameUseCase(nickname)
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
    data class Success(var event: EventModel) : EventUiState
    data class Error(val error: String? = null) : EventUiState
}
