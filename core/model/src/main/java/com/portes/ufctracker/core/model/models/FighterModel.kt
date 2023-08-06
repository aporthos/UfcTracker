package com.portes.ufctracker.core.model.models

data class FighterModel(
    val fighterId: Int,
    var fightId: Int = -1,
    var eventId: Int = -1,
    val fullName: String,
    val imageUrl: String,
    val winner: Boolean,
    val active: Boolean,
    var nickname: String = "",
    var isFighterBet: Boolean,
    var isFightBet: Boolean,
)
