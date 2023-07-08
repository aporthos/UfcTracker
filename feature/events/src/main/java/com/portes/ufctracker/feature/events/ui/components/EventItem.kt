package com.portes.ufctracker.feature.events.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.portes.ufctracker.core.designsystem.R
import com.portes.ufctracker.core.model.models.FighterModel

@Composable
fun RowScope.EventItem(fighter: FighterModel, onClick: (FighterModel) -> Unit) {
    Column(
        modifier = Modifier
            .weight(1f)
            .clickable { onClick(fighter) },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape),
            model = ImageRequest.Builder(LocalContext.current)
                .data(fighter.imageUrl)
                .build(),
            placeholder = painterResource(R.drawable.place_holder),
            error = painterResource(R.drawable.place_holder),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Text(
            textAlign = TextAlign.Center,
            text = fighter.fullName,
        )
    }
}
