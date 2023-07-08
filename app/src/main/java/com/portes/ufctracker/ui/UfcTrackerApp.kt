package com.portes.ufctracker.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.portes.ufctracker.core.designsystem.theme.UfcTrackerTheme
import com.portes.ufctracker.navigation.UfcTrackerHomeSections
import com.portes.ufctracker.navigation.UfcTrackerNavGraph

@Composable
fun UfcTrackerApp() {
    UfcTrackerTheme {
        val appState = rememberUfcTrackerAppState()

        Scaffold(
            scaffoldState = appState.scaffoldState,
            bottomBar = {
                if (appState.shouldShowBottomBar) {
                    UfcTrackerBottomAppBar(
                        tabs = appState.bottomBarTabs,
                        currentRoute = appState.currentRoute,
                        navigateToRoute = {
                            appState.navController.navigate(it)
                        }
                    )
                }
            }) { innerPaddingModifier ->
            UfcTrackerNavGraph(
                modifier = Modifier.padding(innerPaddingModifier),
                navController = appState.navController,
                upPress = appState::upPress
            )
        }
    }
}