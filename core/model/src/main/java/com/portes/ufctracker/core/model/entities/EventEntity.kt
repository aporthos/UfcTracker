package com.portes.ufctracker.core.model.entities

import com.portes.ufctracker.core.model.models.EventModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EventEntity(
    @Json(name = "EventId")
    val eventId: Int?,
    @Json(name = "LeagueId")
    val leagueId: Int?,
    @Json(name = "Name")
    val name: String?,
    @Json(name = "ShortName")
    val shortName: String?,
    @Json(name = "Season")
    val season: Int?,
    @Json(name = "Day")
    val day: String?,
    @Json(name = "DateTime")
    val dateTime: String?,
    @Json(name = "Status")
    val status: String?,
    @Json(name = "Active")
    val active: Boolean?,
    @Json(name = "Fights")
    val fights: List<FightEntity>?,
)

fun EventEntity.toModel() = EventModel(
    eventId = eventId ?: 0,
    leagueId = leagueId ?: 0,
    name = name.orEmpty(),
    shortName = shortName.orEmpty(),
    season = season ?: 0,
    day = day.orEmpty(),
    dateTime = dateTime.orEmpty(),
    status = status.orEmpty(),
    active = active ?: false,
    fights = fights?.map { it.toModel() } ?: emptyList()
)