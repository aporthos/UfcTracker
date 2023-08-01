package com.portes.ufctracker.core.model.entities

data class EventRequest(
    val eventName: String = "",
    val day: String = "",
    val dayTime: String = "",
    val eventId: Int = 0,
)
