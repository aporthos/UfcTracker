package com.portes.ufctracker.core.domain.usecase

import com.portes.ufctracker.core.common.di.IoDispatcher
import com.portes.ufctracker.core.common.domain.FlowUseCase
import com.portes.ufctracker.core.common.models.Result
import com.portes.ufctracker.core.common.today
import com.portes.ufctracker.core.common.todayOrAfter
import com.portes.ufctracker.core.data.repositories.EventsRepository
import com.portes.ufctracker.core.data.repositories.FightBetsRepository
import com.portes.ufctracker.core.model.models.EventModel
import com.portes.ufctracker.core.model.models.EventsCategoriesModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.Date
import javax.inject.Inject

class GetEventsListUseCase @Inject constructor(
    private val repository: EventsRepository,
    private val fightBetsRepository: FightBetsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<Unit, EventsCategoriesModel>(dispatcher) {

    override fun execute(params: Unit): Flow<Result<EventsCategoriesModel>> {
        return combine(
            fightBetsRepository.getFightBetsByEvent(),
            repository.getEventsList()
        ) { fightBets, eventsList ->
            getMappingCategoriesEvents(fightBets, eventsList)
        }
    }

    private fun getMappingCategoriesEvents(
        fightBets: List<Int>,
        eventsList: Result<List<EventModel>>
    ): Result<EventsCategoriesModel> {
        return when (eventsList) {
            is Result.Loading -> Result.Loading
            is Result.Success -> {
                val eventGambledScheduled = mutableListOf<EventModel>()
                val eventGambledFinished = mutableListOf<EventModel>()
                val eventUpcoming = mutableListOf<EventModel>()
                eventsList.data.map { event ->
                    when {
                        fightBets.contains(event.eventId) -> {
                            if (event.day.todayOrAfter()) {
                                eventGambledScheduled.add(event)
                            }
                            if (event.day.before(Date().today())) {
                                eventGambledFinished.add(event)
                            }
                        }
                        !fightBets.contains(event.eventId) -> {
                            if (event.day.todayOrAfter()) {
                                eventUpcoming.add(event)
                            }
                        }
                    }
                }
                Result.Success(
                    EventsCategoriesModel(
                        eventGambledScheduled = eventGambledScheduled,
                        eventGambledFinished = eventGambledFinished,
                        eventUpcoming = eventUpcoming,
                    )
                )
            }
            is Result.Error -> Result.Error(eventsList.exception)
        }
    }
}
