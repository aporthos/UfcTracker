package com.portes.ufctracker.core.model.models

data class FighterModel(
    val fighterId: Int,
    val fullName: String,
    val imageUrl: String,
    val winner: Boolean,
    val moneyline: Int,
    val active: Boolean,
    var nickname: String = "",
    var isSelectedBet: Boolean
)
