package com.portes.ufctracker.core.model.models

data class FightModel(
    val fightId: Int,
    val order: Int,
    val status: String,
    val weightClass: String,
    val cardSegment: String,
    val referee: String,
    val rounds: Int,
    val resultClock: Int,
    val resultRound: Int,
    val resultType: String,
    val winnerId: String,
    val active: Boolean,
    val fighters: List<FighterModel>,
)
