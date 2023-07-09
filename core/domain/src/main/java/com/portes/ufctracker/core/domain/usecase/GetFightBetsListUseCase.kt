package com.portes.ufctracker.core.domain.usecase

import com.portes.ufctracker.core.common.di.IoDispatcher
import com.portes.ufctracker.core.common.domain.FlowSingleUseCase
import com.portes.ufctracker.core.data.repositories.FightBetsRepository
import com.portes.ufctracker.core.model.models.GamblerModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFightBetsListUseCase @Inject constructor(
    private val fightBetsRepository: FightBetsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowSingleUseCase<GetFightBetsListUseCase.Params, List<GamblerModel>>(dispatcher) {

    data class Params(
        val eventId: Int,
    )

    override fun execute(params: Params): Flow<List<GamblerModel>> =
        fightBetsRepository.getFightBetsList(params.eventId)
}
