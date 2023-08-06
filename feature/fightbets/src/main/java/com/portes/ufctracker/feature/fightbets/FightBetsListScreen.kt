package com.portes.ufctracker.feature.fightbets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.portes.ufctracker.core.common.shareMessage
import com.portes.ufctracker.core.designsystem.component.ErrorComponent
import com.portes.ufctracker.core.designsystem.component.ItemFightText
import com.portes.ufctracker.core.designsystem.component.LoadingComponent
import com.portes.ufctracker.core.designsystem.component.OperationsFAB
import com.portes.ufctracker.core.designsystem.component.isScrollingUp
import com.portes.ufctracker.core.designsystem.theme.Purple500
import com.portes.ufctracker.core.domain.usecase.CategoryFightBets
import com.portes.ufctracker.core.domain.usecase.InfoLastFight

@Composable
fun FightBetsListRoute(viewModel: FightBetsListViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val lazyListState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.title_bet_fights)) },
            )
        },
        floatingActionButton = {
            AnimatedVisibility(visible = lazyListState.isScrollingUp()) {
                OperationsFAB(
                    title = "Compartir resultados",
                    icon = Icons.Default.Share,
                    onClick = {
                        context.shareMessage(viewModel.shareResults())
                    }
                )
            }
        },
    ) { innerPaddingModifier ->
        FightBetsScreen(
            modifier = Modifier.padding(innerPaddingModifier),
            isRefreshing = isRefreshing,
            uiState = uiState,
            lazyListState = lazyListState,
            onRefreshing = viewModel::onRefresh
        )
    }
}

@Composable
internal fun FightBetsScreen(
    modifier: Modifier,
    isRefreshing: Boolean,
    uiState: FightBetsListUiState,
    lazyListState: LazyListState,
    onRefreshing: () -> Unit
) {
    when (uiState) {
        FightBetsListUiState.Loading -> LoadingComponent()
        is FightBetsListUiState.Success -> FightBetsList(
            modifier = modifier,
            isRefreshing = isRefreshing,
            infoLastFight = uiState.lastFight,
            onRefreshing = onRefreshing,
            lazyListState = lazyListState
        )
        is FightBetsListUiState.Error -> ErrorComponent(
            modifier = modifier,
            message = uiState.error
        )
    }
}

@Composable
internal fun FightBetsList(
    modifier: Modifier,
    isRefreshing: Boolean,
    infoLastFight: InfoLastFight,
    lazyListState: LazyListState,
    onRefreshing: () -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(isRefreshing, { onRefreshing() })
    Box(
        modifier = modifier
            .pullRefresh(pullRefreshState)
            .fillMaxHeight()
            .padding(8.dp)
    ) {
        LazyColumn(state = lazyListState) {
            item {
                Column(
                    modifier = Modifier.padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Medium),
                        color = Purple500,
                        text = infoLastFight.fightLast?.eventName.orEmpty()
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.caption,
                        text = infoLastFight.fightLast?.dayTime.orEmpty()
                    )
                }
            }
            itemFightsBets(infoLastFight.categoryFightBets)
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

internal fun LazyListScope.itemFightsBets(gamblers: List<CategoryFightBets>) {
    gamblers.forEach {
        item {
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
                        text = "${it.name} ${it.fightsWins} - ${it.fightsLost}",
                        style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                    )
                }
            }
        }
        items(it.fights) { fight ->
            val fighterOne =
                Pair(fight.fighters[0].fullName, fight.fighters[0].isFighterBet)
            val fighterSecond =
                Pair(fight.fighters[1].fullName, fight.fighters[1].isFighterBet)
            ItemFightText(fighterOne, fighterSecond)
        }
    }
}
