package com.portes.ufctracker.core.model.models

data class EventsCategoriesModel(
    val eventGambledScheduled: List<EventModel>,
    val eventGambledFinished: List<EventModel>,
    val eventUpcoming: List<EventModel>,
    val eventPast: List<EventModel>
)
