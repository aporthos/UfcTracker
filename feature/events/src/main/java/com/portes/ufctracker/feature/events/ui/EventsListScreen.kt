package com.portes.ufctracker.feature.events.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.portes.ufctracker.core.designsystem.component.LoadingComponent
import com.portes.ufctracker.core.model.models.EventModel
import com.portes.ufctracker.feature.events.R

@Composable
internal fun EventsListRoute(
    onEventClick: (Int, String) -> Unit,
    viewModel: EventsListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.title_events)) },
            )
        },
    ) { innerPaddingModifier ->
        EventListsScreen(
            modifier = Modifier.padding(innerPaddingModifier),
            uiState = uiState,
            onEventClick = onEventClick
        )
    }
}

@Composable
internal fun EventListsScreen(
    modifier: Modifier,
    uiState: EventsListUiState,
    onEventClick: (Int, String) -> Unit
) {
    when (uiState) {
        EventsListUiState.Loading -> LoadingComponent()
        is EventsListUiState.Success -> EventsList(
            modifier = modifier,
            events = uiState.events,
            onEventClick = onEventClick
        )
        is EventsListUiState.Error -> {
            Text(text = "Error :( ->>${uiState.error}!")
        }
    }
}

@Composable
internal fun EventsList(
    modifier: Modifier,
    events: List<EventModel>,
    onEventClick: (Int, String) -> Unit
) {
    LazyColumn(modifier = modifier.fillMaxHeight(), contentPadding = PaddingValues(16.dp)) {
        items(events, key = { it.eventId }) {
            EventCars(event = it, onEventClick = onEventClick)
        }
    }
}

@Composable
internal fun EventCars(
    event: EventModel,
    onEventClick: (Int, String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEventClick(event.eventId, event.name) }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = event.shortName,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(text = event.name)
        }
    }
}