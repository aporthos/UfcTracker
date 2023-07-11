package com.portes.ufctracker.feature.events.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.portes.ufctracker.core.designsystem.component.ErrorComponent
import com.portes.ufctracker.core.designsystem.component.LoadingComponent
import com.portes.ufctracker.core.designsystem.theme.Purple500
import com.portes.ufctracker.core.designsystem.theme.RoseWhite
import com.portes.ufctracker.core.model.models.FightModel
import com.portes.ufctracker.core.model.models.FighterModel
import com.portes.ufctracker.feature.events.ui.components.FighterComponent

@Composable
internal fun EventFightsListRoute(
    upPress: () -> Unit,
    viewModel: EventsFightsListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val shouldShowAlertDialog = viewModel.shouldShowAlertDialog.collectAsStateWithLifecycle()
    val eventState = rememberEventsAppState()

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
                onClick = {
                    viewModel.createFightBet()
                },
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

    if (shouldShowAlertDialog.value) {
        AlertDialogComponent(
            onDismiss = { viewModel.shouldShowAlertDialog(false) },
            onSaveClick = { nickname ->
                viewModel.shouldShowAlertDialog(false)
                viewModel.saveNickname(nickname)
                viewModel.createFightBet()
            }
        )
    }
}

@Composable
fun rememberEventsAppState() = remember {
    EventsAppState()
}

class EventsAppState {
    var shouldShowAlertDialog by mutableStateOf(false)
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
internal fun AlertDialogComponent(onDismiss: () -> Unit, onSaveClick: (String) -> Unit) {
    var nickname by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismiss() }, properties = DialogProperties()) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(8.dp),
            elevation = 8.dp
        ) {
            Column(
                Modifier
                    .background(Color.White)
            ) {

                Text(
                    text = "Agrega tu nickname o nombre",
                    modifier = Modifier.padding(8.dp)
                )

                OutlinedTextField(
                    value = nickname,
                    onValueChange = { nickname = it }, modifier = Modifier.padding(8.dp),
                    label = { Text("Nickname/Nombre") }
                )

                Row {
                    TextButton(
                        onClick = { onDismiss() },
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)
                    ) {
                        Text(text = "Cancelar")
                    }

                    Button(
                        onClick = {
                            onSaveClick(nickname)
                        },
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)
                    ) {
                        Text(text = "Agregar")
                    }
                }
            }
        }
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
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 0.dp,
        backgroundColor = RoseWhite
    ) {
        Row() {
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
