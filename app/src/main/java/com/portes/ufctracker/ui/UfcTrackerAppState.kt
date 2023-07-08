package com.portes.ufctracker.ui

import android.content.res.Resources
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.portes.ufctracker.navigation.UfcTrackerHomeSections

@Composable
fun rememberUfcTrackerAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
) =
    remember(scaffoldState, navController) {
        UfcTrackerAppState(scaffoldState, navController)
    }

@Stable
class UfcTrackerAppState(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
) {

    val bottomBarTabs = UfcTrackerHomeSections.values()
    private val bottomBarRoutes = bottomBarTabs.map { it.route }

    val shouldShowBottomBar: Boolean
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination?.route in bottomBarRoutes

    val currentRoute: String
        get() = navController.currentDestination?.route ?: UfcTrackerHomeSections.EVENTS.route

    fun upPress() {
        navController.navigateUp()
    }
}

@Composable
@ReadOnlyComposable
private fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}
