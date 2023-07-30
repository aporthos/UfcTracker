package com.portes.ufctracker.core.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.toDate(): Date {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        sdf.parse(this) ?: Date().today()
    } catch (e: Exception) {
        Date().today()
    }
}
