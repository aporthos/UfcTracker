package com.portes.ufctracker.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "fighters",
)

data class FighterLocalEntity(
    @PrimaryKey
    val fighterId: Int,
    val fullName: String,
    val nickname: String,
    val imageUrl: String
)
