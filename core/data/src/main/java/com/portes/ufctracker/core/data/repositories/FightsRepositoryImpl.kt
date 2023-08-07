package com.portes.ufctracker.core.data.repositories

import com.portes.ufctracker.core.common.models.Result
import com.portes.ufctracker.core.common.models.asResult
import com.portes.ufctracker.core.data.datasources.FightsLocalDataSource
import com.portes.ufctracker.core.model.models.FightModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FightsRepositoryImpl @Inject constructor(
    private val fightsLocal: FightsLocalDataSource
) : FightsRepository {
    override fun getFightsList(eventId: Int): Flow<Result<List<FightModel>>> =
        fightsLocal.getFightsByEvent(eventId = eventId).asResult()

    override fun getFightsBet(eventId: Int): Flow<List<FightModel>> =
        fightsLocal.getFightsBet(eventId = eventId)
}

interface FightsRepository {
    fun getFightsList(eventId: Int): Flow<Result<List<FightModel>>>
    fun getFightsBet(eventId: Int): Flow<List<FightModel>>
}
