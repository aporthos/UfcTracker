package com.portes.ufctracker.core.common

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import okio.use
import java.io.File

fun Bitmap.saveAndGetUri(context: Context): Uri {
    val file = File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        "screenshot.png"
    ).writeBitmap(this, Bitmap.CompressFormat.PNG, 85)
    return FileProvider.getUriForFile(
        context,
        context.packageName + ".fileprovider",
        file
    )
}

fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int): File {
    outputStream().use { out ->
        bitmap.compress(format, quality, out)
        out.flush()
    }
    return this
}
