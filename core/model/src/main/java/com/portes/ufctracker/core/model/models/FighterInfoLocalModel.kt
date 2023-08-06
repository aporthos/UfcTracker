package com.portes.ufctracker.core.model.models

import com.portes.ufctracker.core.database.entities.FighterInfoLocalEntity

data class FighterInfoLocalModel(
    val fighterId: Int,
    val fullName: String,
    val nickname: String,
    val imageUrl: String
)

fun FighterInfoLocalEntity.toModel() = FighterInfoLocalModel(
    fighterId = fighterId,
    fullName = fullName,
    nickname = nickname,
    imageUrl = imageUrl
)
