package com.portes.ufctracker.core.data.datasources

import android.content.Context
import com.portes.ufctracker.core.data.convertToList
import com.portes.ufctracker.core.database.FighterDao
import com.portes.ufctracker.core.database.FighterLocalEntity
import com.portes.ufctracker.core.model.entities.FighterEntity
import com.portes.ufctracker.core.model.entities.toEntity
import com.portes.ufctracker.core.model.models.FighterLocaleModel
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FightersLocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fighterDao: FighterDao,
    private val moshi: Moshi,
) : FightersLocalDataSource {
    override suspend fun saveFightersList() {
        val result =
            context.assets.convertToList<FighterEntity>("fightersList.json", moshi)?.map {
                it.toEntity()
            }
        fighterDao.insertOrIgnoreFighters(result)
    }

    override fun getFightersByName(fullName: String): FighterLocaleModel? {
        return fighterDao.getFighterByName(fullName)?.toModel()
    }

    override fun getFightersById(fighterId: Int): FighterLocaleModel? {
        return fighterDao.getFighterById(fighterId)?.toModel()
    }
}

fun FighterLocalEntity.toModel() = FighterLocaleModel(
    fighterId = fighterId,
    fullName = fullName,
    nickname = nickname,
    imageUrl = imageUrl
)

interface FightersLocalDataSource {
    suspend fun saveFightersList()
    fun getFightersByName(fullName: String): FighterLocaleModel?
    fun getFightersById(fighterId: Int): FighterLocaleModel?
}
