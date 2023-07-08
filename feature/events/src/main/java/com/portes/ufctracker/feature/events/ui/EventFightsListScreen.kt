package com.portes.ufctracker.feature.events.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.portes.ufctracker.core.model.models.EventModel
import com.portes.ufctracker.core.model.models.FightModel
import com.portes.ufctracker.core.model.models.FighterModel
import com.portes.ufctracker.core.designsystem.component.LoadingComponent
import com.portes.ufctracker.feature.events.ui.components.EventItem

@Composable
internal fun EventFightsListRoute(
    upPress: () -> Unit,
    viewModel: EventsFightsListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${viewModel.eventName} ${viewModel.eventId}") },
                navigationIcon = {
                    IconButton(onClick = { upPress() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                }
            )
        },
    ) { innerPaddingModifier ->
        EventScreen(
            modifier = Modifier.padding(innerPaddingModifier),
            uiState = uiState,
            onClick = {

            })
    }

}


@Composable
fun FavoritesRoute() {
    Text(text = "Aun no hay nada")
}

@Composable
internal fun EventScreen(
    modifier: Modifier,
    uiState: EventUiState,
    onClick: (EventModel) -> Unit
) {
    val context = LocalContext.current

    when (uiState) {
        EventUiState.Loading -> LoadingComponent()
        is EventUiState.Success -> FightsList(
            modifier = modifier,
            fights = uiState.event.fights,
            onClick = {
                Toast.makeText(
                    context,
                    "Seleccionaste ${it.fullName}",
                    Toast.LENGTH_SHORT
                ).show()
            })
        is EventUiState.Error -> {
            Text(text = "Hello ${uiState.error}!")
        }
    }
}

@Composable
internal fun FightsList(
    modifier: Modifier,
    fights: List<FightModel>,
    onClick: (FighterModel) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 8.dp, bottom = 8.dp)
    ) {
        items(fights, key = { it.fightId }) {
            FightCard(fight = it, onClick = onClick)
        }
    }
}

@Composable
internal fun FightCard(fight: FightModel, onClick: (FighterModel) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            EventItem(
                fighter = fight.fighters[0],
                onClick = onClick
            )
            Text(
                modifier = Modifier
                    .wrapContentHeight()
                    .align(Alignment.CenterVertically),
                textAlign = TextAlign.Center,
                text = "VS"
            )
            EventItem(
                fighter = fight.fighters[1],
                onClick = onClick
            )
        }
    }
}