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
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.portes.ufctracker.core.designsystem.component.ErrorComponent
import com.portes.ufctracker.core.designsystem.component.LoadingComponent
import com.portes.ufctracker.core.model.models.FightModel
import com.portes.ufctracker.core.model.models.FighterModel
import com.portes.ufctracker.feature.events.ui.components.FighterComponent

@Composable
internal fun EventFightsListRoute(
    upPress: () -> Unit,
    viewModel: EventsFightsListViewModel = hiltViewModel(),
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
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Crear apuesta") },
                onClick = viewModel::createFightBet,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = true
    ) { innerPaddingModifier ->
        val context = LocalContext.current
        LaunchedEffect(key1 = true) {
            viewModel.showToast.collect {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
        EventScreen(
            modifier = Modifier.padding(innerPaddingModifier),
            uiState = uiState,
            onFighterClick = viewModel::addFighterToBet,
        )
    }
}

@Composable
internal fun EventScreen(
    modifier: Modifier,
    uiState: EventUiState,
    onFighterClick: (Boolean, Int, FighterModel) -> Unit,
) {
    when (uiState) {
        EventUiState.Loading -> LoadingComponent()
        is EventUiState.Success -> FightsList(
            modifier = modifier,
            fights = uiState.event.fights,
            onFighterClick = onFighterClick,
        )
        is EventUiState.Error -> ErrorComponent(
            modifier = modifier,
            message = uiState.error
        )
    }
}

@Composable
internal fun FightsList(
    modifier: Modifier,
    fights: List<FightModel>,
    onFighterClick: (Boolean, Int, FighterModel) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 8.dp, bottom = 8.dp),
    ) {
        items(fights, key = { it.fightId }) {
            FightCard(
                fight = it,
                onFighterClick = onFighterClick
            )
        }
    }
}

@Composable
internal fun FightCard(fight: FightModel, onFighterClick: (Boolean, Int, FighterModel) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            var isSelectedFighterOne by rememberSaveable { mutableStateOf(fight.fighters[0].isSelectedBet) }
            var isSelectedFighterTwo by rememberSaveable { mutableStateOf(fight.fighters[1].isSelectedBet) }

            FighterComponent(
                isSelected = isSelectedFighterOne,
                fightId = fight.fightId,
                fighter = fight.fighters[0],
                onClick = { fighter ->
                    isSelectedFighterOne = !isSelectedFighterOne
                    isSelectedFighterTwo = false
                    onFighterClick(isSelectedFighterOne, fight.fightId, fighter)
                },
            )
            Text(
                modifier = Modifier
                    .wrapContentHeight()
                    .align(Alignment.CenterVertically),
                textAlign = TextAlign.Center,
                text = "VS",
            )
            FighterComponent(
                isSelected = isSelectedFighterTwo,
                fightId = fight.fightId,
                fighter = fight.fighters[1],
                onClick = { fighter ->
                    isSelectedFighterTwo = !isSelectedFighterTwo
                    isSelectedFighterOne = false
                    onFighterClick(isSelectedFighterTwo, fight.fightId, fighter)
                },
            )
        }
    }
}
