package com.portes.ufctracker.core.model.models

data class FightModel(
    val fightId: Int,
    val order: Int,
    val status: String,
    val active: Boolean,
    val fighters: List<FighterModel>,
)
