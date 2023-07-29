package com.portes.ufctracker.feature.events.ui.components

import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable

@Composable
fun CreateFightBetsFAB(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        text = { Text("Crear apuesta") },
        onClick = {
            onClick()
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }
    )
}
