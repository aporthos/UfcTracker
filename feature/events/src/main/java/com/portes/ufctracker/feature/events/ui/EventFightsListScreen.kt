package com.portes.ufctracker.feature.events.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FabPosition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.portes.ufctracker.core.designsystem.component.ErrorComponent
import com.portes.ufctracker.core.designsystem.component.LoadingComponent
import com.portes.ufctracker.core.designsystem.theme.Purple500
import com.portes.ufctracker.core.designsystem.theme.RoseWhite
import com.portes.ufctracker.core.model.models.FightModel
import com.portes.ufctracker.core.model.models.FighterModel
import com.portes.ufctracker.feature.events.ui.components.AlertDialogComponent
import com.portes.ufctracker.feature.events.ui.components.CreateFightBetsFAB
import com.portes.ufctracker.feature.events.ui.components.FighterComponent
import com.portes.ufctracker.feature.events.ui.components.FightsBetShare
import com.portes.ufctracker.feature.events.ui.components.TopAppBarFightsList
import kotlinx.coroutines.launch

@Composable
internal fun EventFightsListRoute(
    upPress: () -> Unit,
    viewModel: EventsFightsListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBarFightsList(
                name = "${viewModel.eventName} ${viewModel.eventId}",
                upPress = upPress
            )
        },
        floatingActionButton = {
            CreateFightBetsFAB(onClick = viewModel::createFightBet)
        },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = true
    ) { innerPaddingModifier ->
        EventScreen(
            modifier = Modifier.padding(innerPaddingModifier),
            uiState = uiState,
            onFighterClick = viewModel::addFighterToBet,
            onDismissDialog = viewModel::shouldShowAlertDialog,
            onSaveNicknameAndCreateFightBet = viewModel::saveNicknameAndCreateFightBet,
            isClosedBottomSheet = viewModel::resetFighterBet
        )
    }
}

@Composable
internal fun EventScreen(
    modifier: Modifier,
    uiState: EventUiState,
    onFighterClick: (Boolean, Int, FighterModel) -> Unit,
    onDismissDialog: (Boolean) -> Unit,
    onSaveNicknameAndCreateFightBet: (String) -> Unit,
    isClosedBottomSheet: () -> Unit,
) {
    when (uiState) {
        EventUiState.Loading -> LoadingComponent()
        is EventUiState.Success -> {
            EventSuccessScreen(
                modifier = modifier,
                data = uiState.data,
                onFighterClick = onFighterClick,
                onDismissDialog = onDismissDialog,
                onSaveNicknameAndCreateFightBet = onSaveNicknameAndCreateFightBet,
                isClosedBottomSheet = isClosedBottomSheet
            )
        }
        is EventUiState.Error -> ErrorComponent(
            modifier = modifier,
            message = uiState.error
        )
    }
}

@Composable
internal fun EventSuccessScreen(
    modifier: Modifier,
    data: SuccessEvents,
    onFighterClick: (Boolean, Int, FighterModel) -> Unit,
    onDismissDialog: (Boolean) -> Unit,
    onSaveNicknameAndCreateFightBet: (String) -> Unit,
    isClosedBottomSheet: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    FightsList(
        modifier = modifier,
        fights = data.fights,
        onFighterClick = onFighterClick,
    )
    if (data.fightsBets.isNotEmpty()) {
        FightsBetShare(
            fighterBets = data.fightsBets,
            isShowedBottomSheet = { modal ->
                coroutineScope.launch {
                    modal.show()
                }
            },
            isClosedBottomSheet = isClosedBottomSheet
        )
    }

    if (data.shouldShowAlertDialog) {
        AlertDialogComponent(
            onDismissDialog = { onDismissDialog(false) },
            onSaveClick = { nickname ->
                onDismissDialog(false)
                onSaveNicknameAndCreateFightBet(nickname)
            }
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
            .fillMaxHeight()
            .padding(8.dp),
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
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 0.dp,
        backgroundColor = RoseWhite
    ) {
        Row {
            var isSelectedFighterOne by rememberSaveable { mutableStateOf(fight.fighters[0].isSelectedBet) }
            var isSelectedFighterTwo by rememberSaveable { mutableStateOf(fight.fighters[1].isSelectedBet) }

            FighterComponent(
                isSelected = isSelectedFighterOne,
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
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                color = Purple500,
                text = "VS",
            )
            FighterComponent(
                isSelected = isSelectedFighterTwo,
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
