package com.portes.ufctracker.feature.events.ui.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable

@Composable
fun TopAppBarFightsList(
    name: String,
    upPress: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(name)
        },
        navigationIcon = {
            IconButton(onClick = { upPress() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                )
            }
        },
    )
}
