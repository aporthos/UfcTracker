package com.portes.ufctracker.core.domain.usecase

import com.portes.ufctracker.core.common.di.IoDispatcher
import com.portes.ufctracker.core.common.domain.FlowUseCase
import com.portes.ufctracker.core.common.models.Result
import com.portes.ufctracker.core.data.repositories.EventsRepository
import com.portes.ufctracker.core.data.repositories.FightBetsRepository
import com.portes.ufctracker.core.model.models.FightModel
import com.portes.ufctracker.core.model.models.FighterModel
import com.portes.ufctracker.core.model.models.GamblerModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import javax.inject.Inject

class GetFightBetsListUseCase @Inject constructor(
    private val fightBetsRepository: FightBetsRepository,
    private val repository: EventsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<Unit, List<CategoryFightBets>>(
    dispatcher
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(params: Unit): Flow<Result<List<CategoryFightBets>>> {
        // TODO: Check another solution
        val getFightBetsList = fightBetsRepository.getFightLast().flatMapMerge {
            fightBetsRepository.getFightBetsList(it?.eventId ?: 0)
        }

        val getFightsList = fightBetsRepository.getFightLast().flatMapConcat {
            repository.getFightsList(it?.eventId ?: 0)
        }
        return combine(
            getFightBetsList,
            getFightsList
        ) { fightBets, fightsList ->
            when (fightsList) {
                is Result.Loading -> Result.Loading
                is Result.Success -> Result.Success(mapperInCategories(fightBets, fightsList.data))
                is Result.Error -> Result.Error(fightsList.exception)
            }
        }
    }

    private fun mapperInCategories(
        fightBets: List<GamblerModel>,
        fightsList: List<FightModel>
    ): MutableList<CategoryFightBets> {
        val listCategory = mutableListOf<CategoryFightBets>()
        fightBets.forEach { gambler ->
            val fights = ArrayList<FightModel>()
            gambler.fights.forEach { fightGambler ->
                val fighters = ArrayList<FighterModel>()
                fightsList.filter { it.fightId == fightGambler.fightId }
                    .map { fight ->
                        fight.fighters.forEach { fighter ->
                            fighter.isSelectedBet =
                                fighter.fighterId == fightGambler.fighter?.fighterId
                            fighters.add(fighter.copy())
                        }
                        fights.add(fight.copy(fighters = fighters))
                    }
            }
            listCategory.add(
                CategoryFightBets(
                    name = gambler.name,
                    fights = fights
                )
            )
        }
        return listCategory
    }
}

data class CategoryFightBets(
    val name: String,
    val fights: List<FightModel>
)
