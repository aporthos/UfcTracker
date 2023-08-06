package com.portes.ufctracker.core.data.datasources

import android.content.Context
import com.portes.ufctracker.core.data.convertToList
import com.portes.ufctracker.core.database.dao.FightersInfoDao
import com.portes.ufctracker.core.model.entities.FighterEntity
import com.portes.ufctracker.core.model.entities.toEntity
import com.portes.ufctracker.core.model.models.FighterInfoLocalModel
import com.portes.ufctracker.core.model.models.toModel
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FightersInfoLocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fightersInfoDao: FightersInfoDao,
    private val moshi: Moshi,
) : FightersInfoLocalDataSource {
    override suspend fun saveFightersInfoList() {
        val result =
            context.assets.convertToList<FighterEntity>("fightersInfoList.json", moshi)?.map {
                it.toEntity()
            }
        fightersInfoDao.insertOrIgnoreFightersInfo(result)
    }

    override fun getFighterInfoById(fighterId: Int): FighterInfoLocalModel? =
        fightersInfoDao.getFightersInfoById(fighterId)?.toModel()
}

interface FightersInfoLocalDataSource {
    suspend fun saveFightersInfoList()
    fun getFighterInfoById(fighterId: Int): FighterInfoLocalModel?
}