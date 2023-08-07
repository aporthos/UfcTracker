package com.portes.ufctracker.core.data.datasources

import com.portes.ufctracker.core.database.dao.FIGHTER_BET
import com.portes.ufctracker.core.database.dao.FIGHT_BET
import com.portes.ufctracker.core.database.dao.FightersDao
import com.portes.ufctracker.core.database.dao.FightersInfoDao
import com.portes.ufctracker.core.database.entities.FighterLocalEntity
import com.portes.ufctracker.core.model.entities.toEntityLocal
import com.portes.ufctracker.core.model.entities.toFightersModel
import com.portes.ufctracker.core.model.models.FightBetsModel
import com.portes.ufctracker.core.model.models.FightModel
import com.portes.ufctracker.core.model.models.FighterModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FightersLocalDataSourceImpl @Inject constructor(
    private val fightersDao: FightersDao,
    private val fightersInfoDao: FightersInfoDao,
) : FightersLocalDataSource {

    override suspend fun updateFight(
        eventId: Int,
        isFighterBet: Boolean,
        fightId: Int,
        fighterId: Int
    ) {
        fightersDao.resetFightersBet(eventId = eventId, fightId = fightId)
        fightersDao.updateFighterBet(
            eventId = eventId,
            selected = isFighterBet.toFighterBet(),
            fightId = fightId,
            fighterId = fighterId
        )
        fightersDao.checkFightStatusBet(eventId = eventId, fightId = fightId)
    }

    override fun countFightersBetByEvent(eventId: Int): Flow<Int> =
        fightersDao.countFightersBetByEvent(eventId = eventId)

    override suspend fun saveFighters(
        eventId: Int,
        fights: List<FightModel>,
        fightsBet: List<FightBetsModel>
    ): List<Long> {
        val result = processFighters(eventId, fights, fightsBet)
        return fightersDao.insertOrIgnoreFighters(result)
    }

    override fun getFightsByBet(
        eventId: Int,
        isFighterBet: Boolean,
        isFightBet: Boolean,
    ): Flow<List<FighterModel>> {
        return fightersDao.getFightsByBet(
            eventId = eventId,
            isFighterBet = isFighterBet.toFighterBet(),
            isFightBet = isFighterBet.toFightBet()
        ).map {
            it.toFightersModel()
        }
    }

    private fun processFighters(
        eventId: Int,
        fights: List<FightModel>,
        fightsBet: List<FightBetsModel>
    ): List<FighterLocalEntity> {
        val fighters = mutableListOf<FighterLocalEntity>()
        fights.map { fight ->
            fight.fighters.map { fighter ->
                val isFighterBet = fightsBet.any { it.fighter?.fighterId == fighter.fighterId }
                fighter.isFighterBet = isFighterBet
                fighter.isFightBet = fightsBet.any { it.fightId == fight.fightId }
                fighter.nickname =
                    fightersInfoDao.getFightersInfoById(fighterId = fighter.fighterId)?.nickname.orEmpty()
                fighter.fightId = fight.fightId
                fighter.eventId = eventId
                fighters.add(fighter.toEntityLocal())
            }
        }

        return fighters
    }
}

fun Boolean.toFighterBet(): Int =
    if (this) FIGHTER_BET.SELECTED.status else FIGHTER_BET.UNSELECTED.status

fun Boolean.toFightBet(): Int =
    if (this) FIGHT_BET.SELECTED.status else FIGHT_BET.UNSELECTED.status

interface FightersLocalDataSource {
    suspend fun saveFighters(
        eventId: Int,
        fights: List<FightModel>,
        fightsBet: List<FightBetsModel>
    ): List<Long>

    fun getFightsByBet(
        eventId: Int,
        isFighterBet: Boolean,
        isFightBet: Boolean,
    ): Flow<List<FighterModel>>

    suspend fun updateFight(
        eventId: Int,
        isFighterBet: Boolean,
        fightId: Int,
        fighterId: Int
    )

    fun countFightersBetByEvent(eventId: Int): Flow<Int>
}
