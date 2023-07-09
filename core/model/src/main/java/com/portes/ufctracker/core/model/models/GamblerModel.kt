package com.portes.ufctracker.core.model.models

data class GamblerModel(
    val name: String,
    val fights: List<FightBetsModel>,
)

data class FightBetsModel(
    val name: String,
    val fightId: Int,
    val fighter: FighterModel?
)

data class FighterBetRequestModel(
    val fightId: Int,
    val fighter: FighterModel,
)
