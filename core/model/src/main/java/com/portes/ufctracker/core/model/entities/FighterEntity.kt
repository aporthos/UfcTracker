package com.portes.ufctracker.core.model.entities

import com.portes.ufctracker.core.model.models.FighterModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FighterEntity(
    @Json(name = "FighterId")
    val fighterId: Int?,
    @Json(name = "FirstName")
    val firstName: String?,
    @Json(name = "LastName")
    val lastName: String?,
    @Json(name = "PreFightWins")
    val preFightWins: Int?,
    @Json(name = "PreFightLosses")
    val preFightLosses: Int?,
    @Json(name = "PreFightDraws")
    val preFightDraws: Int?,
    @Json(name = "PreFightNoContests")
    val preFightNoContests: Int?,
    @Json(name = "Winner")
    val winner: Boolean?,
    @Json(name = "Moneyline")
    val moneyline: Int?,
    @Json(name = "Active")
    val active: Boolean?
)

fun FighterEntity.toModel() = FighterModel(
    fighterId = fighterId ?: 0,
    fullName = "${firstName.orEmpty()} ${lastName.orEmpty()}",
    preFightWins = preFightWins ?: 0,
    preFightLosses = preFightLosses ?: 0,
    preFightDraws = preFightDraws ?: 0,
    preFightNoContests = preFightNoContests ?: 0,
    imageUrl = "https://fightingtomatoes.com/images/fighters/$firstName$lastName.jpg",
    winner = winner ?: false,
    moneyline = moneyline ?: 0,
    active = active ?: false
)