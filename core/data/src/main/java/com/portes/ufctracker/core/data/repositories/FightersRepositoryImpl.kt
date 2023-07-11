package com.portes.ufctracker.core.data.repositories

import com.portes.ufctracker.core.data.datasources.FightersLocalDataSource
import com.portes.ufctracker.core.model.models.FighterLocaleModel
import javax.inject.Inject

class FightersRepositoryImpl @Inject constructor(
    private val fightersLocalDataSource: FightersLocalDataSource
) : FightersRepository {
    override suspend fun saveFightersList() {
        fightersLocalDataSource.saveFightersList()
    }

    override fun getFightersByName(fullName: String): FighterLocaleModel? {
        return fightersLocalDataSource.getFightersByName(fullName = fullName)
    }

    override fun getFightersById(fighterId: Int): FighterLocaleModel? {
        return fightersLocalDataSource.getFightersById(fighterId = fighterId)
    }
}

interface FightersRepository {
    suspend fun saveFightersList()
    fun getFightersByName(fullName: String): FighterLocaleModel?
    fun getFightersById(fighterId: Int): FighterLocaleModel?
}
