package com.portes.ufctracker.core.common

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.today(): Date {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'00:00:00", Locale.getDefault())
    return sdf.format(Date()).toDate()
}

fun Date.parseString(): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'00:00:00", Locale.getDefault())
        return sdf.format(this)
    } catch (e: ParseException) {
        ""
    }
}

fun Date.parseToString(): String {
    return try {
        val sdf = SimpleDateFormat("MMM dd - HH:mm a", Locale.getDefault())
        sdf.format(this)
    } catch (e: ParseException) {
        ""
    }
}

fun Date.todayOrAfter(): Boolean = after(Date().today()) || this == Date().today()
