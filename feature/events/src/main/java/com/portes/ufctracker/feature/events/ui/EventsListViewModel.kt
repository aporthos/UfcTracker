package com.portes.ufctracker.feature.events.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portes.ufctracker.core.common.models.Result
import com.portes.ufctracker.core.domain.usecase.GetEventsListUseCase
import com.portes.ufctracker.core.model.models.EventModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class EventsListViewModel @Inject constructor(
    getEventsListUseCase: GetEventsListUseCase,
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
}

sealed interface EventsListUiState {
    object Loading : EventsListUiState
    data class Success(var events: List<EventModel>) : EventsListUiState
    data class Error(val error: String? = null) : EventsListUiState
}
