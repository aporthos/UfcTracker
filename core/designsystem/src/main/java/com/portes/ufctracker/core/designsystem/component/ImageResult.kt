package com.portes.ufctracker.core.designsystem.component

import android.graphics.Bitmap

sealed class ImageResult {
    object Initial : ImageResult()
    data class Error(val exception: Exception) : ImageResult()
    data class Success(val data: Bitmap) : ImageResult()
}
