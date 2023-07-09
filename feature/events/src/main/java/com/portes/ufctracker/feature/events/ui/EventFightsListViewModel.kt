package com.portes.ufctracker.feature.events.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portes.ufctracker.core.common.models.Result
import com.portes.ufctracker.core.domain.usecase.AddOrRemoveFightBetsUseCase
import com.portes.ufctracker.core.domain.usecase.GetFightsListUseCase
import com.portes.ufctracker.core.model.models.EventModel
import com.portes.ufctracker.core.model.models.FighterBetRequestModel
import com.portes.ufctracker.core.model.models.FighterModel
import com.portes.ufctracker.feature.events.ui.navigation.EventsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class EventsFightsListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getFightsListUseCase: GetFightsListUseCase,
    private val addOrRemoveFightBetsUseCase: AddOrRemoveFightBetsUseCase,
) : ViewModel() {

    private val eventsArgs: EventsArgs = EventsArgs(savedStateHandle)

    val eventId = eventsArgs.eventId
    val eventName = eventsArgs.name

    private val fighterBetsForAdd = hashMapOf<Int, FighterModel>()
    private val fighterBetsForRemove = hashMapOf<Int, FighterModel>()

    private val _showToast = MutableSharedFlow<String>()
    val showToast: SharedFlow<String> = _showToast.asSharedFlow()

    val uiState: StateFlow<EventUiState> = getFightsListUseCase(
        GetFightsListUseCase.Params(298)
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
        viewModelScope.launch {
            val addFighterBetsAdd = fighterBetsForAdd.map {
                FighterBetRequestModel(it.key, it.value)
            }
            val removeFighterBets = fighterBetsForRemove.map {
                FighterBetRequestModel(it.key, it.value)
            }
            val params = AddOrRemoveFightBetsUseCase.Params(
                eventId = 298,
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
