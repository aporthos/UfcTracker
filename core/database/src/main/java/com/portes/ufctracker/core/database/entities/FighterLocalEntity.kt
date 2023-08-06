package com.portes.ufctracker.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.portes.ufctracker.core.database.TABLE_FIGHTERS

@Entity(
    tableName = TABLE_FIGHTERS,
)
data class FighterLocalEntity(
    @PrimaryKey
    val id: String,
    val fighterId: Int,
    val fightId: Int,
    val eventId: Int,
    val fullName: String,
    val imageUrl: String,
    val winner: Boolean,
    val active: Boolean,
    var nickname: String = "",
    var isFighterBet: Boolean,
    var isFightBet: Boolean = false
)
