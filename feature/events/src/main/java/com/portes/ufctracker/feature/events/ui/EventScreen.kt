package com.portes.ufctracker.feature.events.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.portes.ufctracker.core.model.models.EventModel
import com.portes.ufctracker.core.model.models.FightModel
import com.portes.ufctracker.core.model.models.FighterModel


@Composable
fun EventRoute(upPress: () -> Unit, viewModel: EventsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiStateEvent.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TopAppBar") },
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
fun EventScreen(
    modifier: Modifier,
    uiState: EventUiState,
    onClick: (EventModel) -> Unit
) {
    val context = LocalContext.current

    when (uiState) {
        EventUiState.Loading -> {

        }
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
fun FightsList(modifier: Modifier, fights: List<FightModel>, onClick: (FighterModel) -> Unit) {
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
fun FightCard(fight: FightModel, onClick: (FighterModel) -> Unit) {
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

@Composable
fun RowScope.EventItem(fighter: FighterModel, onClick: (FighterModel) -> Unit) {
    Column(
        modifier = Modifier
            .weight(1f)
            .clickable { onClick(fighter) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val asyncPainter = rememberAsyncImagePainter(fighter.imageUrl)
        Image(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape),
            painter = asyncPainter,
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        Text(
            textAlign = TextAlign.Center,
            text = fighter.fullName
        )
    }
}