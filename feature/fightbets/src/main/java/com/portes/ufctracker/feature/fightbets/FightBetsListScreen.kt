package com.portes.ufctracker.feature.fightbets

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.portes.ufctracker.core.designsystem.component.LoadingComponent
import com.portes.ufctracker.core.model.models.GamblerModel

@Composable
fun FightBetsListRoute(viewModel: FightBetsListViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.title_bet_fights)) },
            )
        },
    ) { innerPaddingModifier ->
        FightBetsScreen(
            modifier = Modifier.padding(innerPaddingModifier),
            uiState = uiState,
        )
    }
}

@Composable
internal fun FightBetsScreen(modifier: Modifier, uiState: FightBetsListUiState) {
    when (uiState) {
        FightBetsListUiState.Loading -> LoadingComponent()
        is FightBetsListUiState.Success -> FightBetsList(modifier, uiState.events)
        is FightBetsListUiState.Error -> {
            Text(text = "Error :( ->>${uiState.error}!")
        }
    }
}

@Composable
fun FightBetsList(modifier: Modifier, gamblers: List<GamblerModel>) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 8.dp, bottom = 8.dp),
    ) {
        items(gamblers, key = { it.name }) {
            Text(text = ":) ->>${it.name} ${it.fights}")
            Text(text = ":) ->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        }
    }
}
