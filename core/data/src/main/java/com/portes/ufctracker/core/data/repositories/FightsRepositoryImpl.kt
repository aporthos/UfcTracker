package com.portes.ufctracker.core.data.repositories

import com.portes.ufctracker.core.common.models.Result
import com.portes.ufctracker.core.common.models.asResult
import com.portes.ufctracker.core.data.datasources.FightsLocalDataSource
import com.portes.ufctracker.core.model.models.FightModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FightsRepositoryImpl @Inject constructor(
    private val fightersInfoLocalDataSource: FightsLocalDataSource
) : FightsRepository {
    override fun getFightsList(eventId: Int): Flow<Result<List<FightModel>>> {
        return fightersInfoLocalDataSource.getFightsById(eventId).asResult()
    }
}

interface FightsRepository {
    fun getFightsList(eventId: Int): Flow<Result<List<FightModel>>>
}
