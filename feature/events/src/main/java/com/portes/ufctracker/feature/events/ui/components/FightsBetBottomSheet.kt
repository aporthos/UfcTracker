package com.portes.ufctracker.feature.events.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.portes.ufctracker.core.common.saveAndGetUri
import com.portes.ufctracker.core.common.shareImage
import com.portes.ufctracker.core.designsystem.component.ImageResult
import com.portes.ufctracker.core.designsystem.component.ItemFightText
import com.portes.ufctracker.core.designsystem.component.ScreenshotComponent
import com.portes.ufctracker.core.designsystem.component.rememberScreenshotState
import com.portes.ufctracker.core.designsystem.theme.Purple500
import com.portes.ufctracker.core.model.models.FightModel
import timber.log.Timber

@Composable
fun FightsBetShare(
    fighterBets: List<FightModel>,
    isShowedBottomSheet: (ModalBottomSheetState) -> Unit,
    isClosedBottomSheet: () -> Unit,
) {
    var isSheetFullScreen by remember { mutableStateOf(false) }

    val state =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmValueChange = { sheetValue ->
                when (sheetValue) {
                    ModalBottomSheetValue.Expanded -> isSheetFullScreen = true
                    ModalBottomSheetValue.HalfExpanded -> isSheetFullScreen = false
                    else -> isClosedBottomSheet()
                }
                true
            },
            skipHalfExpanded = false
        )
    FightsBetBottomSheet(
        state = state,
        isSheetFullScreen = isSheetFullScreen,
        fighterBets = fighterBets,
        content = {
            isShowedBottomSheet(state)
        }
    )
}

@Composable
internal fun FightsBetBottomSheet(
    state: ModalBottomSheetState,
    isSheetFullScreen: Boolean,
    fighterBets: List<FightModel>,
    content: @Composable () -> Unit
) {
    val roundedCornerRadius = if (isSheetFullScreen) 0.dp else 8.dp
    val context = LocalContext.current
    val screenshotState = rememberScreenshotState()
    val imageResult: ImageResult = screenshotState.imageState.value

    ModalBottomSheetLayout(
        modifier = Modifier.zIndex(1f),
        sheetState = state,
        sheetShape = RoundedCornerShape(
            topStart = roundedCornerRadius,
            topEnd = roundedCornerRadius
        ),
        sheetContent = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Chip(
                    onClick = screenshotState::capture,
                    colors = ChipDefaults.chipColors(
                        backgroundColor = Purple500,
                        contentColor = Color.White,
                        leadingIconContentColor = Color.White
                    ),
                    leadingIcon = {
                        Icon(Icons.Filled.Share, contentDescription = null)
                    }
                ) {
                    Text(text = "Compartir")
                }
            }
            ScreenshotComponent(screenshotState) {
                Column(
                    modifier = Modifier.background(color = Color.White)
                ) {
                    fighterBets.forEach {
                        val fighterOne = Pair(it.fighters[0].fullName, it.fighters[0].isFighterBet)
                        val fighterSecond =
                            Pair(it.fighters[1].fullName, it.fighters[1].isFighterBet)
                        ItemFightText(fighterOne, fighterSecond)
                    }
                }
            }
            when (imageResult) {
                is ImageResult.Initial -> Unit
                is ImageResult.Success -> {
                    val uri = imageResult.data.saveAndGetUri(context)
                    context.shareImage(uri)
                }
                is ImageResult.Error -> {
                    Toast.makeText(context, "Ocurrio un error al generar el SS", Toast.LENGTH_LONG)
                        .show()
                    Timber.e("FightsBetBottomSheet: ${imageResult.exception.message}")
                }
            }
        }
    ) {
        content()
    }
}
