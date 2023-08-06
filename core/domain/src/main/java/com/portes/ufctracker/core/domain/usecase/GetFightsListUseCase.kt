package com.portes.ufctracker.core.domain.usecase

import com.portes.ufctracker.core.common.di.IoDispatcher
import com.portes.ufctracker.core.common.domain.FlowUseCase
import com.portes.ufctracker.core.common.models.Result
import com.portes.ufctracker.core.data.repositories.FightsRepository
import com.portes.ufctracker.core.model.models.FightModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFightsListUseCase @Inject constructor(
    private val fightersRepository: FightsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<GetFightsListUseCase.Params, List<FightModel>>(dispatcher) {

    data class Params(
        val eventId: Int
    )

    override fun execute(params: Params): Flow<Result<List<FightModel>>> =
        fightersRepository.getFightsList(params.eventId)
}
