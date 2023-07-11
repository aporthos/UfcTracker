package com.portes.ufctracker.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FighterDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreFighters(entities: List<FighterLocalEntity>?): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreFighter(entities: FighterLocalEntity): Long

    @Query(
        value = """
        SELECT * FROM fighters
         WHERE fullName LIKE  :fullName || '%'
    """
    )
    fun getFighterByName(fullName: String): FighterLocalEntity?

    @Query(
        value = """
        SELECT * FROM fighters
         WHERE fighterId = :fighterId
    """
    )
    fun getFighterById(fighterId: Int): FighterLocalEntity?
}
