package com.portes.ufctracker.feature.events.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.portes.ufctracker.feature.events.ui.EventFightsListRoute
import com.portes.ufctracker.feature.events.ui.EventsListRoute

enum class HomeDestinations(val destination: String) {
    HOME("home"),
    HOME_EVENTS("home/events"),
    FIGHTERS("home/fighters"),
}

fun NavGraphBuilder.eventsGraph(
    onBackClick: () -> Unit,
    onEventClick: (Int, String) -> Unit,
) {
    navigation(
        route = HomeDestinations.HOME.destination,
        startDestination = HomeDestinations.HOME_EVENTS.destination,
    ) {
        composable(HomeDestinations.HOME_EVENTS.destination) {
            EventsListRoute(onEventClick = { eventId, name ->
                onEventClick(eventId, name)
            })
        }
        composable(
            route = "${HomeDestinations.FIGHTERS}/{$EVENT_ID}/{$EVENT_NAME}",
            arguments = listArguments,
        ) {
            EventFightsListRoute(upPress = onBackClick)
        }
    }
}

val listArguments = listOf(
    navArgument(EVENT_ID) { type = NavType.IntType },
    navArgument(EVENT_NAME) { type = NavType.StringType },
)

fun NavController.navigateToEvent(eventId: Int, name: String) {
    navigate("${HomeDestinations.FIGHTERS}/$eventId/$name")
}