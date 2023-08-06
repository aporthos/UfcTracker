package com.portes.ufctracker.core.data.datasources

import com.portes.ufctracker.core.database.dao.FIGHTER_BET
import com.portes.ufctracker.core.database.dao.FIGHT_BET
import com.portes.ufctracker.core.database.dao.FightersDao
import com.portes.ufctracker.core.database.dao.FightersInfoDao
import com.portes.ufctracker.core.model.entities.toEntityLocal
import com.portes.ufctracker.core.model.entities.toFightersModel
import com.portes.ufctracker.core.model.models.FightBetsModel
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

    override suspend fun saveFighters(
        eventId: Int,
        fightId: Int,
        fights: List<FighterModel>,
        fightsBet: List<FightBetsModel>
    ): List<Long> {
        val result = fights.map { fighter ->
            fighter.isFighterBet = fightsBet.any { it.fighter?.fighterId == fighter.fighterId }
            fighter.nickname =
                fightersInfoDao.getFightersInfoById(fighterId = fighter.fighterId)?.nickname.orEmpty()
            fighter.fightId = fightId
            fighter.eventId = eventId
            fighter.toEntityLocal()
        }
        val insert = fightersDao.insertOrIgnoreFighters(result)
        fightersDao.checkFightStatusBet(eventId = eventId, fightId = fightId)
        return insert
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
}

fun Boolean.toFighterBet(): Int =
    if (this) FIGHTER_BET.SELECTED.status else FIGHTER_BET.UNSELECTED.status

fun Boolean.toFightBet(): Int =
    if (this) FIGHT_BET.SELECTED.status else FIGHT_BET.UNSELECTED.status

interface FightersLocalDataSource {
    suspend fun saveFighters(
        eventId: Int,
        fightId: Int,
        fights: List<FighterModel>,
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
}
