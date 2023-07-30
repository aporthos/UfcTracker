package com.portes.ufctracker.feature.fightbets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.portes.ufctracker.core.designsystem.component.ErrorComponent
import com.portes.ufctracker.core.designsystem.component.ItemFightText
import com.portes.ufctracker.core.designsystem.component.LoadingComponent
import com.portes.ufctracker.core.designsystem.theme.Purple500
import com.portes.ufctracker.core.domain.usecase.CategoryFightBets

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
        is FightBetsListUiState.Error -> ErrorComponent(
            modifier = modifier,
            message = uiState.error
        )
    }
}

@Composable
fun FightBetsList(modifier: Modifier, gamblers: List<CategoryFightBets>) {
    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
            .padding(8.dp),
    ) {
        items(gamblers, key = { it.name }) {
            Card(
                shape = RoundedCornerShape(8.dp),
                elevation = 0.dp,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        text = it.name,
                        style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Medium),
                        color = Purple500
                    )
                }
            }
            it.fights.forEach { figth ->
                val fighterOne = Pair(figth.fighters[0].fullName, figth.fighters[0].isSelectedBet)
                val fighterSecond =
                    Pair(figth.fighters[1].fullName, figth.fighters[1].isSelectedBet)
                ItemFightText(fighterOne, fighterSecond)
            }
        }
    }
}
