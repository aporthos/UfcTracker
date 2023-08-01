package com.portes.ufctracker.core.domain.usecase

import com.portes.ufctracker.core.common.di.IoDispatcher
import com.portes.ufctracker.core.common.domain.FlowUseCase
import com.portes.ufctracker.core.common.models.Result
import com.portes.ufctracker.core.data.repositories.FightBetsRepository
import com.portes.ufctracker.core.data.repositories.PreferencesRepository
import com.portes.ufctracker.core.model.entities.EventRequest
import com.portes.ufctracker.core.model.models.FighterBetRequestModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddOrRemoveFightBetsUseCase @Inject constructor(
    private val fightBetsRepository: FightBetsRepository,
    private val preferencesRepository: PreferencesRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<AddOrRemoveFightBetsUseCase.Params, Boolean>(dispatcher) {
    data class Params(
        val eventRequest: EventRequest,
        val addFighterBets: List<FighterBetRequestModel>,
        val removeFighterBets: List<FighterBetRequestModel>
    )

    override fun execute(params: Params): Flow<Result<Boolean>> =
        fightBetsRepository.addOrRemoveFightBetsList(
            eventRequest = params.eventRequest,
            nickname = preferencesRepository.getNickname(),
            addFighterBets = params.addFighterBets,
            removeFighterBets = params.removeFighterBets
        )
}
