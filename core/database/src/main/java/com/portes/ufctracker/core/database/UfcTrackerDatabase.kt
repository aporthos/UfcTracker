package com.portes.ufctracker.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.portes.ufctracker.core.database.dao.FightersDao
import com.portes.ufctracker.core.database.dao.FightersInfoDao
import com.portes.ufctracker.core.database.dao.FightsDao
import com.portes.ufctracker.core.database.entities.FightLocalEntity
import com.portes.ufctracker.core.database.entities.FighterInfoLocalEntity
import com.portes.ufctracker.core.database.entities.FighterLocalEntity

@Database(
    entities = [FighterInfoLocalEntity::class, FightLocalEntity::class, FighterLocalEntity::class],
    version = 1,
    exportSchema = false
)
abstract class UfcTrackerDatabase : RoomDatabase() {
    abstract fun fighterInfoDao(): FightersInfoDao
    abstract fun fightDao(): FightsDao
    abstract fun fightersDao(): FightersDao
}
