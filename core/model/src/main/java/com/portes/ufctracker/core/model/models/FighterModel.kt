package com.portes.ufctracker.core.model.models

data class FighterModel(
    val fighterId: Int,
    val fullName: String,
    val imageUrl: String,
    val preFightWins: Int,
    val preFightLosses: Int,
    val preFightDraws: Int,
    val preFightNoContests: Int,
    val winner: Boolean,
    val moneyline: Int,
    val active: Boolean,
    var isSelectedBet: Boolean = false
)
