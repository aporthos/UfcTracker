package com.portes.ufctracker.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.portes.ufctracker.core.database.TABLE_FIGHTS

@Entity(
    tableName = TABLE_FIGHTS,
)
data class FightLocalEntity(
    @PrimaryKey
    val fightId: Int,
    val eventId: Int,
    val order: Int = 0,
    val status: String = "",
    val active: Boolean,
)
