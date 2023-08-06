package com.portes.ufctracker.core.domain.usecase

import com.portes.ufctracker.core.common.di.IoDispatcher
import com.portes.ufctracker.core.common.domain.FlowUseCase
import com.portes.ufctracker.core.common.models.Result
import com.portes.ufctracker.core.data.repositories.EventsRepository
import com.portes.ufctracker.core.data.repositories.FightBetsRepository
import com.portes.ufctracker.core.model.entities.EventRequest
import com.portes.ufctracker.core.model.models.FightModel
import com.portes.ufctracker.core.model.models.FighterModel
import com.portes.ufctracker.core.model.models.GamblerModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import javax.inject.Inject

class GetFightBetsListUseCase @Inject constructor(
    private val fightBetsRepository: FightBetsRepository,
    private val repository: EventsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<Unit, InfoLastFight>(
    dispatcher
) {

    override fun execute(params: Unit): Flow<Result<InfoLastFight>> {
        // TODO: Check another solution
        val getFightLast = fightBetsRepository.getFightLast()

        val getFightBetsList = getFightLast.flatMapMerge {
            fightBetsRepository.getFightBetsList(it?.eventId ?: 0)
        }

        val getFightsList = getFightLast.flatMapConcat {
            repository.getFightsList(it?.eventId ?: 0)
        }
        return combine(
            getFightLast,
            getFightBetsList,
            getFightsList
        ) { fightLast, fightBets, fightsList ->
            when (fightsList) {
                Result.Loading -> Result.Loading
                is Result.Success -> Result.Success(
                    mapperInCategories(
                        fightLast,
                        fightBets,
                        fightsList.data
                    )
                )
                is Result.Error -> Result.Error(fightsList.exception)
            }
        }
    }

    private fun mapperInCategories(
        fightLast: EventRequest?,
        fightBets: List<GamblerModel>,
        fightsList: List<FightModel>
    ): InfoLastFight {
        val categoriesFightBets = mutableListOf<CategoryFightBets>()
        fightBets.forEach { gambler ->
            val fights = ArrayList<FightModel>()
            val totalFights = gambler.fights.size
            var fightsWins = 0
            gambler.fights.forEach { fightGambler ->
                val fighters = ArrayList<FighterModel>()
                fightsList.filter { it.fightId == fightGambler.fightId }
                    .map { fight ->
                        fight.fighters.forEach { fighter ->
                            fighter.isFighterBet =
                                fighter.fighterId == fightGambler.fighter?.fighterId
                            if (fighter.winner && fighter.fighterId == fightGambler.fighter?.fighterId) {
                                fightsWins++
                            }
                            fighters.add(fighter.copy())
                        }
                        fights.add(fight.copy(fighters = fighters))
                    }
            }
            categoriesFightBets.add(
                CategoryFightBets(
                    name = gambler.name,
                    fights = fights,
                    fightsWins = fightsWins,
                    fightsLost = totalFights - fightsWins
                )
            )
        }
        categoriesFightBets.sortByDescending { it.fightsWins }
        return InfoLastFight(fightLast, categoriesFightBets)
    }
}

data class InfoLastFight(
    val fightLast: EventRequest?,
    val categoryFightBets: List<CategoryFightBets>
)

data class CategoryFightBets(
    val name: String,
    val fights: List<FightModel>,
    val fightsWins: Int,
    val fightsLost: Int,
)
