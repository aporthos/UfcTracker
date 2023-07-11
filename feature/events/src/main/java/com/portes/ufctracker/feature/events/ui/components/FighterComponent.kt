package com.portes.ufctracker.feature.events.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.portes.ufctracker.core.designsystem.R
import com.portes.ufctracker.core.designsystem.theme.GreenChecked
import com.portes.ufctracker.core.designsystem.theme.Purple500
import com.portes.ufctracker.core.model.models.FighterModel

@Composable
fun RowScope.FighterComponent(
    isSelected: Boolean,
    fighter: FighterModel,
    onClick: (FighterModel) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(8.dp)
            .clickable { onClick(fighter) },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        val modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .border(
                border = if (isSelected) BorderStroke(
                    6.dp,
                    GreenChecked
                ) else BorderStroke(6.dp, Color.Unspecified),
                shape = CircleShape
            )

        AsyncImage(
            modifier = modifier,
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
            style = MaterialTheme.typography.h6
        )
        Text(
            textAlign = TextAlign.Center,
            text = fighter.nickName,
            style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Bold),
            color = Purple500
        )
    }
}
