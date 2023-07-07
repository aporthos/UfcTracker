package com.portes.ufctracker.core.domain.usecase

import com.portes.ufctracker.core.common.di.IoDispatcher
import com.portes.ufctracker.core.common.domain.FlowUseCase
import com.portes.ufctracker.core.data.repositories.EventsRepository
import com.portes.ufctracker.core.model.models.EventModel
import kotlinx.coroutines.CoroutineDispatcher
import com.portes.ufctracker.core.common.models.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEventsListUseCase @Inject constructor(
    private val repository: EventsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<Unit, List<EventModel>>(dispatcher) {

    override fun execute(params: Unit): Flow<Result<List<EventModel>>> = repository.getEventsList()
}