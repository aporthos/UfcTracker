package com.portes.ufctracker.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.portes.ufctracker.R
import com.portes.ufctracker.feature.events.ui.EventRoute
import com.portes.ufctracker.feature.events.ui.EventsListRoute
import com.portes.ufctracker.feature.events.ui.FavoritesRoute

enum class UfcTrackerHomeSections(
    @StringRes val title: Int,
    val icon: ImageVector,
    val route: String
) {
    EVENTS(R.string.home_event, Icons.Outlined.Home, "home/events"),
    FAVORITES(R.string.home_current_bet, Icons.Outlined.Favorite, "home/myCurrentBet")
}

enum class UfcTrackerDirections(
    val route: String
) {
    EVENT("home/event"),
}

@Composable
fun UfcTrackerNavGraph(
    modifier: Modifier,
    startDestination: String,
    navController: NavHostController,
    upPress: () -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(UfcTrackerHomeSections.EVENTS.route) {
            EventsListRoute(onClick = {
                navController.navigate(UfcTrackerDirections.EVENT.route)
            })
        }

        composable(UfcTrackerHomeSections.FAVORITES.route) {
            FavoritesRoute()
        }

        composable(UfcTrackerDirections.EVENT.route) {
            EventRoute(upPress = upPress)
        }
    }
}