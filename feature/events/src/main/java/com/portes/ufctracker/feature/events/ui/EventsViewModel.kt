package com.portes.ufctracker.feature.events.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portes.ufctracker.core.common.models.Result
import com.portes.ufctracker.core.domain.usecase.GetEventUseCase
import com.portes.ufctracker.core.domain.usecase.GetEventsListUseCase
import com.portes.ufctracker.core.model.models.EventModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    getEventsListUseCase: GetEventsListUseCase,
    getEventUseCase: GetEventUseCase
) : ViewModel() {

    val uiState: StateFlow<EventsListUiState> = getEventsListUseCase(Unit).map { result ->
        when (result) {
            is Result.Success -> EventsListUiState.Success(result.data)
            is Result.Error -> EventsListUiState.Error(result.exception)
            Result.Loading -> EventsListUiState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        initialValue = EventsListUiState.Loading,
        started = SharingStarted.WhileSubscribed(),
    )

    val uiStateEvent: StateFlow<EventUiState> = getEventUseCase(298).map { result ->
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

sealed interface EventsListUiState {
    object Loading : EventsListUiState
    data class Success(var events: List<EventModel>) : EventsListUiState
    data class Error(val error: String? = null) : EventsListUiState
}

sealed interface EventUiState {
    object Loading : EventUiState
    data class Success(var event: EventModel) : EventUiState
    data class Error(val error: String? = null) : EventUiState
}