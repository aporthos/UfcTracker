package com.portes.ufctracker.core.domain.usecase

import com.portes.ufctracker.core.common.di.IoDispatcher
import com.portes.ufctracker.core.common.domain.FlowUseCase
import com.portes.ufctracker.core.common.models.Result
import com.portes.ufctracker.core.data.repositories.FightBetsRepository
import com.portes.ufctracker.core.data.repositories.FightersRepository
import com.portes.ufctracker.core.data.repositories.PreferencesRepository
import com.portes.ufctracker.core.model.entities.EventRequest
import com.portes.ufctracker.core.model.models.FighterBetRequestModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import javax.inject.Inject

class AddOrRemoveFightBetsUseCase @Inject constructor(
    private val fightBetsRepository: FightBetsRepository,
    private val preferencesRepository: PreferencesRepository,
    private val fightersRepository: FightersRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<AddOrRemoveFightBetsUseCase.Params, Boolean>(dispatcher) {
    data class Params(
        val eventRequest: EventRequest,
    )

    override fun execute(params: Params): Flow<Result<Boolean>> {
        val fightsBetsAdd =
            fightersRepository.getFightsByBet(
                isSelectedBet = true,
                isFightBet = true,
                eventId = params.eventRequest.eventId
            )
        val fightsBetsRemove =
            fightersRepository.getFightsByBet(
                isSelectedBet = false,
                isFightBet = false,
                eventId = params.eventRequest.eventId
            )

        return combine(fightsBetsAdd, fightsBetsRemove) { add, remove ->
            val addFighterBets = add.map { fighter ->
                FighterBetRequestModel(fighter.fightId, fighter)
            }
            val removeFighterBets = remove.map { fighter ->
                FighterBetRequestModel(fighter.fightId, fighter)
            }

            Pair(addFighterBets, removeFighterBets)
        }.flatMapMerge { fighterBets ->
            fightBetsRepository.addOrRemoveFightBetsList(
                eventRequest = params.eventRequest,
                nickname = preferencesRepository.getNickname(),
                addFighterBets = fighterBets.first,
                removeFighterBets = fighterBets.second
            )
        }
    }

}
