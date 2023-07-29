package com.portes.ufctracker.core.designsystem.component

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.drawToBitmap

@Composable
fun rememberScreenshotState() = remember { ScreenshotState() }

class ScreenshotState {
    val imageState = mutableStateOf<ImageResult>(ImageResult.Initial)
    var callback: (() -> Unit)? = null
    fun capture() {
        callback?.invoke()
    }
}

@Composable
fun ScreenshotComponent(screenshotState: ScreenshotState, content: @Composable () -> Unit) {
    val context = LocalContext.current
    val composeView = remember { ComposeView(context = context) }

    AndroidView(
        factory = {
            composeView.apply {
                setContent {
                    content()
                }
            }
        },
        modifier = Modifier.verticalScroll(rememberScrollState())
    )
    screenshotState.callback = {
        try {
            screenshotState.imageState.value = ImageResult.Success(composeView.drawToBitmap())
        } catch (e: Exception) {
            screenshotState.imageState.value = ImageResult.Error(e)
        }
    }
}
