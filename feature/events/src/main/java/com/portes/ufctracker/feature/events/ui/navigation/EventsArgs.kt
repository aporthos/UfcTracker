package com.portes.ufctracker.feature.events.ui.navigation

import androidx.lifecycle.SavedStateHandle

internal const val EVENT_ID = "EVENT_ID"
internal const val EVENT_NAME = "EVENT_NAME"
internal const val EVENT_DATE = "EVENT_DATE"

internal class EventsArgs(val eventId: Int, val name: String, val eventDate: String) {
    constructor(savedStateHandle: SavedStateHandle) :
        this(
            checkNotNull(savedStateHandle[EVENT_ID]),
            checkNotNull(savedStateHandle[EVENT_NAME]),
            checkNotNull(savedStateHandle[EVENT_DATE]),
        )
}
