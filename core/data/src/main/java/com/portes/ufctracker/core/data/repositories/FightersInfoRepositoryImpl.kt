package com.portes.ufctracker.core.data.repositories

import com.portes.ufctracker.core.data.datasources.FightersInfoLocalDataSource
import com.portes.ufctracker.core.model.models.FighterInfoLocalModel
import javax.inject.Inject

class FightersInfoRepositoryImpl @Inject constructor(
    private val fightersInfoLocalDataSource: FightersInfoLocalDataSource
) : FightersInfoRepository {
    override suspend fun saveFightersInfoList() {
        fightersInfoLocalDataSource.saveFightersInfoList()
    }

    override fun getFighterInfoById(fighterId: Int): FighterInfoLocalModel? =
        fightersInfoLocalDataSource.getFighterInfoById(fighterId = fighterId)
}

interface FightersInfoRepository {
    suspend fun saveFightersInfoList()
    fun getFighterInfoById(fighterId: Int): FighterInfoLocalModel?
}
