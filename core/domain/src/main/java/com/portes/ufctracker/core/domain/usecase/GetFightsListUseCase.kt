package com.portes.ufctracker.core.domain.usecase

import com.portes.ufctracker.core.common.di.IoDispatcher
import com.portes.ufctracker.core.common.domain.FlowUseCase
import com.portes.ufctracker.core.common.models.Result
import com.portes.ufctracker.core.data.repositories.FightersRepository
import com.portes.ufctracker.core.data.repositories.FightsRepository
import com.portes.ufctracker.core.model.models.FightModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetFightsListUseCase @Inject constructor(
    private val repository: FightsRepository,
    private val fightersRepository: FightersRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<GetFightsListUseCase.Params, Fights>(dispatcher) {

    data class Params(
        val eventId: Int
    )

    override fun execute(params: Params): Flow<Result<Fights>> {
        return combine(
            repository.getFightsList(params.eventId),
            repository.getFightsBet(params.eventId),
            fightersRepository.countFightersBetByEvent(params.eventId)
        ) { fightsBet, fightsNew, showButtonCreate ->
            when (fightsBet) {
                Result.Loading -> Result.Loading
                is Result.Success -> Result.Success(
                    Fights(
                        fightsBet = fightsBet.data,
                        fightsNew = fightsNew,
                        showButtonCreate = showButtonCreate > 0
                    )
                )
                is Result.Error -> Result.Error(fightsBet.exception)
            }
        }
    }
}

data class Fights(
    val fightsBet: List<FightModel>,
    val fightsNew: List<FightModel>,
    val showButtonCreate: Boolean
)
