package com.portes.ufctracker.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FighterLocalEntity::class], version = 1)
abstract class UfcTrackerDatabase : RoomDatabase() {
    abstract fun fighterDao(): FighterDao
}
