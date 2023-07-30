package com.portes.ufctracker.core.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.portes.ufctracker.core.designsystem.theme.Purple500
import com.portes.ufctracker.core.designsystem.theme.RoseWhite

@Composable
fun ItemFightText(fighterOne: Pair<String, Boolean>, fighterSecond: Pair<String, Boolean>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 0.dp,
        backgroundColor = RoseWhite
    ) {
        Row {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 4.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                text = fighterOne.first,
                textDecoration = if (fighterOne.second) TextDecoration.LineThrough else null
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6,
                color = Purple500,
                text = "VS",
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 4.dp),
                text = fighterSecond.first,
                textDecoration = if (fighterSecond.second) TextDecoration.LineThrough else null,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
            )
        }
    }
}
