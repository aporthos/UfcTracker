package com.portes.ufctracker.feature.events.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.portes.ufctracker.core.common.parseString
import com.portes.ufctracker.core.common.parseToString
import com.portes.ufctracker.core.designsystem.component.ErrorComponent
import com.portes.ufctracker.core.designsystem.component.LoadingComponent
import com.portes.ufctracker.core.designsystem.theme.GreenChecked
import com.portes.ufctracker.core.designsystem.theme.Purple500
import com.portes.ufctracker.core.designsystem.theme.RoseWhite
import com.portes.ufctracker.core.model.models.EventModel
import com.portes.ufctracker.core.model.models.EventsCategoriesModel
import com.portes.ufctracker.feature.events.R
import com.portes.ufctracker.core.designsystem.R as DesygnSystem

@Composable
internal fun EventsListRoute(
    onEventClick: (Int, String, String) -> Unit,
    viewModel: EventsListViewModel = hiltViewModel(),
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
            onEventClick = onEventClick,
        )
    }
}

@Composable
internal fun EventListsScreen(
    modifier: Modifier,
    uiState: EventsListUiState,
    onEventClick: (Int, String, String) -> Unit,
) {
    when (uiState) {
        EventsListUiState.Loading -> LoadingComponent()
        is EventsListUiState.Success -> EventsList(
            modifier = modifier,
            events = uiState.events,
            onEventClick = onEventClick,
        )
        is EventsListUiState.Error -> ErrorComponent(
            modifier = modifier,
            message = uiState.error
        )
    }
}

@Composable
internal fun EventsList(
    modifier: Modifier,
    events: EventsCategoriesModel,
    onEventClick: (Int, String, String) -> Unit,
) {
    var isExpandedEventGambledScheduled by remember { mutableStateOf(true) }
    var isExpandedEventUpcoming by remember { mutableStateOf(true) }
    var isExpandedEventGambledFinished by remember { mutableStateOf(false) }
    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
            .padding(8.dp)
    ) {
        itemEventsCategories(
            events = events.eventGambledScheduled,
            isExpanded = isExpandedEventGambledScheduled,
            titleCategory = "Apuestas abiertas",
            onEventClick = onEventClick,
            onCardClick = {
                isExpandedEventGambledScheduled = !isExpandedEventGambledScheduled
            }
        )
        itemEventsCategories(
            events = events.eventUpcoming,
            isExpanded = isExpandedEventUpcoming,
            titleCategory = "Proximos eventos",
            onEventClick = onEventClick,
            onCardClick = {
                isExpandedEventUpcoming = !isExpandedEventUpcoming
            }
        )
        itemEventsCategories(
            events = events.eventGambledFinished,
            isExpanded = isExpandedEventGambledFinished,
            titleCategory = "Apuestas anteriores",
            onEventClick = onEventClick,
            onCardClick = {
                isExpandedEventGambledFinished = !isExpandedEventGambledFinished
            }
        )
    }
}

internal fun LazyListScope.itemEventsCategories(
    events: List<EventModel>,
    isExpanded: Boolean,
    titleCategory: String,
    onEventClick: (Int, String, String) -> Unit,
    onCardClick: () -> Unit
) {
    item {
        if (events.isNotEmpty()) {
            Card(
                shape = RoundedCornerShape(8.dp),
                elevation = 0.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onCardClick()
                    }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        text = titleCategory,
                        style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Medium),
                        color = Purple500
                    )
                    Spacer(Modifier.weight(1f))
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                }
            }
        }
    }
    items(events, key = { it.eventId }) {
        if (isExpanded) {
            EventCards(event = it, onEventClick = onEventClick)
        }
    }
}

@Composable
internal fun EventCards(
    event: EventModel,
    onEventClick: (Int, String, String) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onEventClick(event.eventId, event.name, event.day.parseString()) },
        shape = RoundedCornerShape(8.dp),
        elevation = 0.dp,
        backgroundColor = RoseWhite
    ) {
        Column {
            Text(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 8.dp),
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                text = event.name,
            )
            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth(.5f)
                ) {
                    Text(
                        text = event.shortName,
                        style = MaterialTheme.typography.h5
                    )
                    Text(
                        text = event.dateTime.parseToString(),
                        style = MaterialTheme.typography.caption
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier.padding(end = 8.dp, bottom = 8.dp)
                ) {
                    val modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(
                            border = if (true) BorderStroke(
                                2.dp,
                                GreenChecked
                            ) else BorderStroke(6.dp, Color.Unspecified),
                            shape = CircleShape
                        )
                    AsyncImage(
                        modifier = modifier
                            .clip(CircleShape)
                            .zIndex(1f),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(event.fightStar.firstOrNull()?.imageUrl)
                            .build(),
                        placeholder = painterResource(DesygnSystem.drawable.place_holder),
                        error = painterResource(DesygnSystem.drawable.place_holder),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )
                    AsyncImage(
                        modifier = Modifier
                            .padding(start = 56.dp)
                            .size(80.dp)
                            .clip(CircleShape),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("https://fightingtomatoes.com/images/fighters/YairRodriguez.jpg")
                            .build(),
                        placeholder = painterResource(DesygnSystem.drawable.place_holder),
                        error = painterResource(DesygnSystem.drawable.place_holder),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }
    }
}
