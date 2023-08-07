package com.portes.ufctracker.core.data.datasources

import com.portes.ufctracker.core.database.dao.FightsDao
import com.portes.ufctracker.core.model.entities.toEntityLocal
import com.portes.ufctracker.core.model.entities.toFightsModel
import com.portes.ufctracker.core.model.models.FightModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FightsLocalDataSourceImpl @Inject constructor(
    private val fightDao: FightsDao,
) : FightsLocalDataSource {
    override suspend fun saveFights(eventId: Int, fights: List<FightModel>): List<Long> {
        val result = fights.map {
            it.eventId = eventId
            it.toEntityLocal()
        }
        return fightDao.insertOrIgnoreFights(result)
    }

    override fun getFightsByEvent(eventId: Int): Flow<List<FightModel>> {
        return fightDao.getFightsById(eventId).map {
            it.toFightsModel()
        }.distinctUntilChanged()
    }

    override fun getFightsBet(eventId: Int): Flow<List<FightModel>> {
        return fightDao.getFightsBet(eventId).map {
            it.toFightsModel()
        }
    }
}

interface FightsLocalDataSource {
    suspend fun saveFights(eventId: Int, fights: List<FightModel>): List<Long>
    fun getFightsByEvent(eventId: Int): Flow<List<FightModel>>
    fun getFightsBet(eventId: Int): Flow<List<FightModel>>
}
