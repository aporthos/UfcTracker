package com.portes.ufctracker.feature.events.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FabPosition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.portes.ufctracker.core.designsystem.component.OperationsFAB
import com.portes.ufctracker.core.designsystem.component.isScrollingUp
import com.portes.ufctracker.core.designsystem.theme.Purple500
import com.portes.ufctracker.core.designsystem.theme.RoseWhite
import com.portes.ufctracker.core.model.models.FightModel
import com.portes.ufctracker.core.model.models.FighterModel
import com.portes.ufctracker.feature.events.ui.components.AlertDialogSaveFightBets
import com.portes.ufctracker.feature.events.ui.components.AlertDialogSaveNickname
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
    val lazyListState = rememberLazyListState()
    var showButtonCreateFightBets by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBarFightsList(
                name = "${viewModel.eventName} ${viewModel.eventId}",
                upPress = viewModel::onBackPressed
            )
        },
        floatingActionButton = {
            if (showButtonCreateFightBets) {
                AnimatedVisibility(visible = lazyListState.isScrollingUp()) {
                    OperationsFAB(
                        title = "Crear apuesta",
                        icon = Icons.Default.Add,
                        onClick = viewModel::createFightBet
                    )
                }
                viewModel.reset()
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = true
    ) { innerPaddingModifier ->
        EventScreen(
            modifier = Modifier.padding(innerPaddingModifier),
            uiState = uiState,
            onFighterClick = viewModel::addFighterToBet,
            onDismissAlertSaveNickname = viewModel::shouldShowAlertSaveNickname,
            onDismissAlertSaveFightBets = viewModel::shouldShowAlertSaveFightBets,
            onSaveNicknameAndCreateFightBet = viewModel::saveNicknameAndCreateFightBet,
            isClosedBottomSheet = viewModel::resetFighterBet,
            lazyListState = lazyListState,
            upPress = upPress,
            onSaveFightsBets = viewModel::createFightBet,
            showButtonCreateFightBets = {
                showButtonCreateFightBets = it
            },
        )
    }

    BackHandler(enabled = true) {
        viewModel.onBackPressed()
    }
}

@Composable
internal fun EventScreen(
    modifier: Modifier,
    uiState: EventUiState,
    onFighterClick: (Boolean, FighterModel) -> Unit,
    onDismissAlertSaveNickname: (Boolean) -> Unit,
    onDismissAlertSaveFightBets: (Boolean?) -> Unit,
    onSaveNicknameAndCreateFightBet: (String) -> Unit,
    isClosedBottomSheet: () -> Unit,
    lazyListState: LazyListState,
    upPress: () -> Unit,
    onSaveFightsBets: () -> Unit,
    showButtonCreateFightBets: (Boolean) -> Unit,
) {
    when (uiState) {
        EventUiState.Loading -> LoadingComponent()
        is EventUiState.Success -> {
            showButtonCreateFightBets(uiState.data.shouldShowButtonCreate)
            EventSuccessScreen(
                modifier = modifier,
                data = uiState.data,
                onFighterClick = onFighterClick,
                onDismissAlertSaveNickname = onDismissAlertSaveNickname,
                onDismissAlertSaveFightBets = onDismissAlertSaveFightBets,
                onSaveNicknameAndCreateFightBet = onSaveNicknameAndCreateFightBet,
                isClosedBottomSheet = isClosedBottomSheet,
                lazyListState = lazyListState,
                upPress = upPress,
                onSaveFightsBets = onSaveFightsBets,
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
    onFighterClick: (Boolean, FighterModel) -> Unit,
    onDismissAlertSaveNickname: (Boolean) -> Unit,
    onDismissAlertSaveFightBets: (Boolean?) -> Unit,
    onSaveNicknameAndCreateFightBet: (String) -> Unit,
    isClosedBottomSheet: () -> Unit,
    lazyListState: LazyListState,
    upPress: () -> Unit,
    onSaveFightsBets: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    FightsList(
        modifier = modifier,
        fights = data.fights,
        onFighterClick = onFighterClick,
        lazyListState = lazyListState
    )
    if (data.shouldShowShare) {
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

    if (data.shouldShowAlertSaveNickname) {
        AlertDialogSaveNickname(
            onDismissDialog = { onDismissAlertSaveNickname(false) },
            onSaveClick = { nickname ->
                onDismissAlertSaveNickname(false)
                onSaveNicknameAndCreateFightBet(nickname)
            }
        )
    }

    if (data.shouldShowAlertSaveFightBets != null) {
        if (data.shouldShowAlertSaveFightBets) {
            AlertDialogSaveFightBets(
                onDismissDialog = {
                    onDismissAlertSaveFightBets(null)
                    upPress()
                },
                onSaveClick = { ->
                    onDismissAlertSaveFightBets(null)
                    onSaveFightsBets()
                }
            )
        } else {
            upPress()
        }
    }
}

@Composable
internal fun FightsList(
    modifier: Modifier,
    fights: List<FightModel>,
    lazyListState: LazyListState,
    onFighterClick: (Boolean, FighterModel) -> Unit,
) {
    LazyColumn(
        state = lazyListState,
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
internal fun FightCard(fight: FightModel, onFighterClick: (Boolean, FighterModel) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 0.dp,
        backgroundColor = RoseWhite
    ) {
        Row {

            fight.fighters.getOrNull(0)?.let {
                FighterComponent(
                    isSelected = it.isFighterBet,
                    fighter = it,
                    onClick = { fighter ->
                        onFighterClick(!it.isFighterBet, fighter)
                    },
                )
            }

            Text(
                modifier = Modifier
                    .wrapContentHeight()
                    .align(Alignment.CenterVertically),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                color = Purple500,
                text = "VS",
            )
            fight.fighters.getOrNull(1)?.let {
                FighterComponent(
                    isSelected = it.isFighterBet,
                    fighter = it,
                    onClick = { fighter ->
                        onFighterClick(!it.isFighterBet, fighter)
                    },
                )
            }
        }
    }
}
