package com.portes.ufctracker.core.database.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import com.portes.ufctracker.core.database.TABLE_FIGHTS
import com.portes.ufctracker.core.database.entities.FightLocalEntity
import com.portes.ufctracker.core.database.entities.FighterLocalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FightsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrIgnoreFights(entities: List<FightLocalEntity>?): List<Long>

    @Transaction
    @Query(
        value = """
            SELECT * FROM $TABLE_FIGHTS 
            WHERE eventId = :eventId 
            AND active != 0 
            ORDER BY `order`
    """
    )
    fun getFightById(eventId: Int): Flow<List<FightWithFightersEntity>>
}

data class FightWithFightersEntity(
    @Embedded val fightEntity: FightLocalEntity,
    @Relation(
        entityColumn = "fightId",
        parentColumn = "fightId"
    )
    val fighterEntities: List<FighterLocalEntity>
)
