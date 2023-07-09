package com.portes.ufctracker.core.model.entities

import com.portes.ufctracker.core.model.models.FightBetsModel

data class FightBetsEntity(
    val name: String? = null,
    val fightId: Int? = null,
    val fighter: FighterEntity? = null,
)

fun FightBetsEntity.toModel() = FightBetsModel(
    name = name.orEmpty(),
    fightId = fightId ?: 0,
    fighter = fighter?.toModel(),
)
