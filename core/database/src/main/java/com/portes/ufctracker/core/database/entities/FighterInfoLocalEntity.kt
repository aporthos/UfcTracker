package com.portes.ufctracker.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.portes.ufctracker.core.database.TABLE_FIGHTERS_INFO

@Entity(
    tableName = TABLE_FIGHTERS_INFO,
)
data class FighterInfoLocalEntity(
    @PrimaryKey
    val fighterId: Int,
    val fullName: String,
    val nickname: String,
    val imageUrl: String
)
