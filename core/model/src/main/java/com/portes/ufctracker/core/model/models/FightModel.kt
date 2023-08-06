package com.portes.ufctracker.core.model.models

data class FightModel(
    val fightId: Int,
    var eventId: Int = 0,
    val order: Int,
    val status: String,
    val active: Boolean,
    val fighters: List<FighterModel>,
)
