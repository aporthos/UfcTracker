package com.portes.ufctracker.core.domain.usecase

import com.portes.ufctracker.core.common.di.IoDispatcher
import com.portes.ufctracker.core.common.domain.FlowUseCase
import com.portes.ufctracker.core.common.models.Result
import com.portes.ufctracker.core.data.repositories.EventsRepository
import com.portes.ufctracker.core.data.repositories.FightBetsRepository
import com.portes.ufctracker.core.model.models.EventModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetFightsListUseCase @Inject constructor(
    private val repository: EventsRepository,
    private val fightBetsRepository: FightBetsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<GetFightsListUseCase.Params, EventModel>(dispatcher) {

    data class Params(
        val eventId: Int
    )

    override fun execute(params: Params): Flow<Result<EventModel>> {
        return combine(
            fightBetsRepository.getMyFightBets(params.eventId),
            repository.getFightsList(params.eventId)
        ) { fightBets, fightsList ->
            if (fightsList is Result.Success) {
                fightsList.data.fights.map {
                    it.fighters.map { fighters ->
                        fighters.isSelectedBet = fightBets.contains(fighters.fighterId)
                    }
                }
            }
            fightsList
        }
    }
}