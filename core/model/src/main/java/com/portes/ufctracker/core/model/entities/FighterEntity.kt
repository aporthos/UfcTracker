package com.portes.ufctracker.core.model.entities

import com.google.firebase.firestore.PropertyName
import com.portes.ufctracker.core.database.FighterLocalEntity
import com.portes.ufctracker.core.model.models.FighterModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FighterEntity(
    @Json(name = "FighterId")
    val fighterId: Int? = 0,
    @Json(name = "FirstName")
    val firstName: String? = "",
    @Json(name = "LastName")
    val lastName: String? = "",
    @Json(name = "nickname")
    val nickname: String? = "",
    @Json(name = "PreFightWins")
    val preFightWins: Int? = 0,
    @Json(name = "PreFightLosses")
    val preFightLosses: Int? = 0,
    @Json(name = "PreFightDraws")
    val preFightDraws: Int? = 0,
    @Json(name = "PreFightNoContests")
    val preFightNoContests: Int? = 0,
    @Json(name = "Winner")
    val winner: Boolean? = false,
    @Json(name = "Moneyline")
    val moneyline: Int? = 0,
    @Json(name = "Active")
    val active: Boolean? = false,
    @Json(name = "selectedBet")
    @get:PropertyName("selectedBet")
    @set:PropertyName("selectedBet")
    var isSelectedBet: Boolean? = false,
    @Json(name = "imageUrl")
    var imageUrl: String? = "",
    @Json(name = "fullName")
    var fullName: String? = "",
)

fun FighterEntity.toModel() = FighterModel(
    fighterId = fighterId ?: 0,
    fullName = "${firstName.orEmpty()} ${lastName.orEmpty()}",
    imageUrl = "https://fightingtomatoes.com/images/fighters/$firstName$lastName.jpg",
    winner = winner ?: false,
    moneyline = moneyline ?: 0,
    active = active ?: false,
    isSelectedBet = isSelectedBet ?: false
)

fun FighterEntity.toEntity() = FighterLocalEntity(
    fighterId = fighterId ?: 0,
    fullName = "${firstName.orEmpty()} ${lastName.orEmpty()}",
    nickname = nickname.orEmpty(),
    imageUrl = "https://fightingtomatoes.com/images/fighters/$firstName$lastName.jpg"
)
