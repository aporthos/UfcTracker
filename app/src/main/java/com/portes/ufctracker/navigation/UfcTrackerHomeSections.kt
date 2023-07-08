package com.portes.ufctracker.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.portes.ufctracker.R

enum class UfcTrackerHomeSections(
    @StringRes val title: Int,
    val icon: ImageVector,
    val route: String,
) {
    EVENTS(R.string.home_event, Icons.Outlined.Home, "home/events"),
    CURRENT_BET(R.string.home_current_bet, Icons.Outlined.Favorite, "home/currentBet"),
}
