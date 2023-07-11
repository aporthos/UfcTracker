package com.portes.ufctracker.core.model.models

import com.portes.ufctracker.core.model.entities.StatusEvent

data class EventModel(
    val eventId: Int,
    val leagueId: Int,
    val name: String,
    val shortName: String,
    val season: Int,
    val day: String,
    val dateTime: String,
    val status: StatusEvent,
    val active: Boolean,
    val fightStar: MutableList<FighterModel> = mutableListOf(),
    val fights: List<FightModel>,
)
