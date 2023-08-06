package com.portes.ufctracker.core.model.entities

import com.google.firebase.firestore.PropertyName
import com.portes.ufctracker.core.database.entities.FighterInfoLocalEntity
import com.portes.ufctracker.core.database.entities.FighterLocalEntity
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
    @Json(name = "Nickname")
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
    @get:PropertyName("fighterBet")
    @set:PropertyName("fighterBet")
    var isFighterBet: Boolean? = false,
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
    active = active ?: false,
    isFighterBet = isFighterBet ?: false,
    isFightBet = false
)

fun FighterEntity.toEntity() = FighterInfoLocalEntity(
    fighterId = fighterId ?: 0,
    fullName = "${firstName.orEmpty()} ${lastName.orEmpty()}",
    nickname = nickname.orEmpty(),
    imageUrl = "https://fightingtomatoes.com/images/fighters/$firstName$lastName.jpg"
)

fun FighterModel.toEntityLocal() = FighterLocalEntity(
    id = "${fightId}_$fighterId",
    fighterId = fighterId,
    fightId = fightId,
    fullName = fullName,
    nickname = nickname,
    imageUrl = imageUrl,
    winner = winner,
    active = active,
    isFighterBet = isFighterBet,
    isFightBet = isFightBet,
    eventId = eventId
)
