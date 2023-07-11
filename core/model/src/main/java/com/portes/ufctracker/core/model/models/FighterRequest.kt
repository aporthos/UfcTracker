package com.portes.ufctracker.core.model.models

data class FighterRequest(
    val name: String,
    val fightId: Int,
    val fighter: FighterModel
)
