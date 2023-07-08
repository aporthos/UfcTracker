package com.portes.ufctracker.ui

import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.portes.ufctracker.navigation.UfcTrackerHomeSections

@Composable
fun UfcTrackerBottomAppBar(
    tabs: Array<UfcTrackerHomeSections>,
    currentRoute: String,
    navigateToRoute: (String) -> Unit,
) {
    BottomAppBar {
        val currentSection = tabs.first { it.route == currentRoute }

        tabs.forEach { section ->
            val selected = section == currentSection
            BottomNavigationItem(
                selected = selected,
                onClick = {
                    navigateToRoute(section.route)
                },
                label = {
                    Text(text = stringResource(id = section.title))
                },
                icon = {
                    Icon(
                        imageVector = section.icon,
                        contentDescription = null,
                    )
                },
            )
        }
    }
}
