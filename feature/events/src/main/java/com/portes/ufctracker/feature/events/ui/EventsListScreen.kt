package com.portes.ufctracker.feature.events.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.portes.ufctracker.core.model.models.EventModel

@Composable
fun EventsListRoute(onClick: (EventModel) -> Unit, viewModel: EventsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TopAppBar") },
            )
        },
    ) { innerPaddingModifier ->
        EventListsScreen(
            modifier = Modifier.padding(innerPaddingModifier),
            uiState = uiState,
            onClick = onClick
        )
    }
}

@Composable
fun EventListsScreen(
    modifier: Modifier,
    uiState: EventsListUiState,
    onClick: (EventModel) -> Unit
) {
    when (uiState) {
        EventsListUiState.Loading -> {

        }
        is EventsListUiState.Success -> EventsList(events = uiState.events, onClick = onClick)
        is EventsListUiState.Error -> {
            Text(text = "Hello ${uiState.error}!")
        }
    }
}

@Composable
fun EventsList(events: List<EventModel>, onClick: (EventModel) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxHeight(), contentPadding = PaddingValues(16.dp)) {
        items(events, key = { it.eventId }) {
            EventCars(event = it, onClick = onClick)
        }
    }
}

@Composable
fun EventCars(event: EventModel, onClick: (EventModel) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(event) }
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