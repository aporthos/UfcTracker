package com.portes.ufctracker.core.designsystem.component

import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun OperationsFAB(title: String, icon: ImageVector, onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        text = { Text(title) },
        onClick = {
            onClick()
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        },

    )
}
