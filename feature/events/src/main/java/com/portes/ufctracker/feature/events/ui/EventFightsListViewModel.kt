package com.portes.ufctracker.feature.events.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portes.ufctracker.core.common.models.Result
import com.portes.ufctracker.core.domain.usecase.GetEventUseCase
import com.portes.ufctracker.core.model.models.EventModel
import com.portes.ufctracker.feature.events.ui.navigation.EventsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class EventsFightsListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getEventUseCase: GetEventUseCase,
) : ViewModel() {

    private val eventsArgs: EventsArgs = EventsArgs(savedStateHandle)

    val eventId = eventsArgs.eventId
    val eventName = eventsArgs.name

    val uiState: StateFlow<EventUiState> = getEventUseCase(eventsArgs.eventId).map { result ->
        when (result) {
            is Result.Success -> EventUiState.Success(result.data)
            is Result.Error -> EventUiState.Error(result.exception)
            Result.Loading -> EventUiState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        initialValue = EventUiState.Loading,
        started = SharingStarted.WhileSubscribed(),
    )
}

sealed interface EventUiState {
    object Loading : EventUiState
    data class Success(var event: EventModel) : EventUiState
    data class Error(val error: String? = null) : EventUiState
}
