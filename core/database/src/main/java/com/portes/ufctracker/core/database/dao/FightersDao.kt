package com.portes.ufctracker.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.portes.ufctracker.core.database.TABLE_FIGHTERS
import com.portes.ufctracker.core.database.entities.FighterLocalEntity
import kotlinx.coroutines.flow.Flow

enum class FIGHT_BET(val status: Int) {
    SELECTED(1),
    UNSELECTED(0)
}

enum class FIGHTER_BET(val status: Int) {
    SELECTED(1),
    UNSELECTED(0)
}

@Dao
abstract class FightersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertOrIgnoreFighters(entities: List<FighterLocalEntity>?): List<Long>

    @Query(
        value = """
            UPDATE $TABLE_FIGHTERS SET isFighterBet = 0 
            WHERE eventId = :eventId
            AND fightId = :fightId
    """
    )
    abstract suspend fun resetFightersBet(
        eventId: Int,
        fightId: Int,
    )

    @Query(
        value = """
            UPDATE $TABLE_FIGHTERS SET isFighterBet = :selected 
            WHERE eventId = :eventId
            AND fighterId = :fighterId 
            AND fightId = :fightId
    """
    )
    abstract suspend fun updateFighterBet(
        eventId: Int,
        selected: Int,
        fightId: Int,
        fighterId: Int
    )

    @Query(
        value = """
            UPDATE $TABLE_FIGHTERS SET isFightBet = :isFightBet 
            WHERE eventId = :eventId
            AND fightId = :fightId
    """
    )
    abstract suspend fun updateFightBet(
        eventId: Int,
        fightId: Int,
        isFightBet: Int = FIGHTER_BET.UNSELECTED.status
    )

    @Query(
        value = """ 
            SELECT COUNT(*) FROM $TABLE_FIGHTERS 
            WHERE eventId = :eventId 
            AND fightId = :fightId
            AND isFighterBet = 1
    """
    )
    abstract suspend fun countFightersBet(eventId: Int, fightId: Int): Int

    suspend fun checkFightStatusBet(eventId: Int, fightId: Int) {
        if (countFightersBet(eventId, fightId) > 0) {
            updateFightBet(
                eventId = eventId,
                fightId = fightId,
                isFightBet = FIGHT_BET.SELECTED.status
            )
        } else {
            updateFightBet(
                eventId = eventId,
                fightId = fightId,
                isFightBet = FIGHT_BET.UNSELECTED.status
            )
        }
    }

    @Transaction
    @Query(
        value = """ 
            SELECT * FROM $TABLE_FIGHTERS 
            WHERE eventId = :eventId 
            AND isFighterBet = :isFighterBet 
            AND isFightBet = :isFightBet
    """
    )
    abstract fun getFightsByBet(
        eventId: Int,
        isFighterBet: Int,
        isFightBet: Int,
    ): Flow<List<FighterLocalEntity>>

}

