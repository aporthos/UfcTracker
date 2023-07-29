package com.portes.ufctracker.core.model.entities

import com.portes.ufctracker.core.model.models.FightModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FightEntity(
    @Json(name = "FightId")
    val fightId: Int?,
    @Json(name = "Order")
    val order: Int?,
    @Json(name = "Status")
    val status: String?,
    @Json(name = "WeightClass")
    val weightClass: String?,
    @Json(name = "CardSegment")
    val cardSegment: String?,
    @Json(name = "Referee")
    val referee: String?,
    @Json(name = "Rounds")
    val rounds: Int?,
    @Json(name = "ResultClock")
    val resultClock: Int?,
    @Json(name = "ResultRound")
    val resultRound: Int?,
    @Json(name = "ResultType")
    val resultType: String?,
    @Json(name = "WinnerId")
    val winnerId: String?,
    @Json(name = "Active")
    val active: Boolean?,
    @Json(name = "Fighters")
    val fighters: List<FighterEntity>?,
)

fun FightEntity.toModel() = FightModel(
    fightId = fightId ?: 0,
    order = order ?: 0,
    status = status.orEmpty(),
    active = active ?: false,
    fighters = fighters?.map { it.toModel() } ?: emptyList(),
)
