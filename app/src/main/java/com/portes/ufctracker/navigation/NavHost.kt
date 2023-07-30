package com.portes.ufctracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.portes.ufctracker.feature.events.ui.navigation.HomeDestinations
import com.portes.ufctracker.feature.events.ui.navigation.eventsGraph
import com.portes.ufctracker.feature.events.ui.navigation.navigateToEvent
import com.portes.ufctracker.feature.fightbets.FightBetsListRoute

@Composable
fun UfcTrackerNavGraph(
    modifier: Modifier,
    navController: NavHostController,
    upPress: () -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = HomeDestinations.HOME.destination,
    ) {
        eventsGraph(
            onBackClick = upPress,
            onEventClick = navController::navigateToEvent
        )
        composable(UfcTrackerHomeSections.FIGHT_BETS.route) {
            FightBetsListRoute()
        }
    }
}
