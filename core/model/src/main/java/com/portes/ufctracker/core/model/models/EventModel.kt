package com.portes.ufctracker.core.model.models

import java.util.Date

data class EventModel(
    val eventId: Int,
    val name: String,
    val shortName: String,
    val season: Int,
    val dateTime: Date,
    val day: Date,
    val active: Boolean,
    val fightStar: MutableList<FighterModel> = mutableListOf(),
    val fights: List<FightModel>,
)
