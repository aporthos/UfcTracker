package com.portes.ufctracker.core.common

import android.content.Context
import android.net.Uri
import androidx.core.app.ShareCompat

fun Context.shareImage(uri: Uri) {
    ShareCompat.IntentBuilder(this)
        .setType("image/*")
        .addStream(uri)
        .setChooserTitle("Compartir")
        .startChooser()
}

fun Context.shareMessage(message: String) {
    ShareCompat.IntentBuilder(this)
        .setType("text/plain")
        .setText(message)
        .setChooserTitle("Compartir")
        .startChooser()
}
