package com.portes.ufctracker.core.data.repositories

import com.portes.ufctracker.core.common.models.Result
import com.portes.ufctracker.core.data.datasources.FightersLocalDataSource
import com.portes.ufctracker.core.data.datasources.FightsLocalDataSource
import com.portes.ufctracker.core.model.models.FighterModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import timber.log.Timber
import javax.inject.Inject

class FightersRepositoryImpl @Inject constructor(
    private val fightBetsRepository: FightBetsRepository,
    private val fightersLocal: FightersLocalDataSource,
    private val fightsLocal: FightsLocalDataSource,
    private val eventsRepository: EventsRepository,
    private val preferencesRepository: PreferencesRepository,
) : FightersRepository {

    override suspend fun getAndSaveFights(eventId: Int): Flow<Result<Boolean>> {
        return combine(
            fightBetsRepository.getMyFightBets(
                eventId = eventId,
                nickname = preferencesRepository.getNickname()
            ),
            eventsRepository.getFightsList(eventId),
        ) { firestore, remote ->
            when (remote) {
                is Result.Loading -> Result.Loading
                is Result.Success -> {
                    val onlyActives = remote.data
                    fightsLocal.saveFights(eventId, onlyActives)
                    fightersLocal.saveFighters(
                        eventId = eventId,
                        fights = onlyActives,
                        fightsBet = firestore
                    )
                    Result.Success(true)
                }
                is Result.Error -> {
                    Timber.e("getFightsList: ${remote.exception}")
                    Result.Error(remote.exception)
                }
            }
        }
    }

    override fun countFightersBetByEvent(eventId: Int): Flow<Int> =
        fightersLocal.countFightersBetByEvent(eventId = eventId)

    override fun getFightsByBet(
        eventId: Int,
        isSelectedBet: Boolean,
        isFightBet: Boolean,
    ): Flow<List<FighterModel>> = fightersLocal.getFightsByBet(
        eventId = eventId,
        isFighterBet = isFightBet,
        isFightBet = isSelectedBet,
    )

    override suspend fun updateFight(
        eventId: Int,
        isSelectedBet: Boolean,
        fightId: Int,
        fighterId: Int
    ) {
        fightersLocal.updateFight(eventId, isSelectedBet, fightId, fighterId)
    }
}

interface FightersRepository {
    fun getFightsByBet(
        eventId: Int,
        isSelectedBet: Boolean,
        isFightBet: Boolean,
    ): Flow<List<FighterModel>>

    suspend fun updateFight(
        eventId: Int,
        isSelectedBet: Boolean,
        fightId: Int,
        fighterId: Int
    )

    suspend fun getAndSaveFights(eventId: Int): Flow<Result<Boolean>>
    fun countFightersBetByEvent(eventId: Int): Flow<Int>
}
