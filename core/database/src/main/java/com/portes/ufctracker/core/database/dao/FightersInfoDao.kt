package com.portes.ufctracker.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.portes.ufctracker.core.database.TABLE_FIGHTERS_INFO
import com.portes.ufctracker.core.database.entities.FighterInfoLocalEntity

@Dao
interface FightersInfoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreFightersInfo(entities: List<FighterInfoLocalEntity>?): List<Long>

    @Query(
        value = """
            SELECT * FROM $TABLE_FIGHTERS_INFO
            WHERE fighterId = :fighterId
    """
    )
    fun getFightersInfoById(fighterId: Int): FighterInfoLocalEntity?
}
