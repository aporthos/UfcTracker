package com.portes.ufctracker.core.model.models

data class EventModel(
    val eventId: Int,
    val leagueId: Int,
    val name: String,
    val shortName: String,
    val season: Int,
    val day: String,
    val dateTime: String,
    val status: String,
    val active: Boolean,
    val fights: List<FightModel>,
)
