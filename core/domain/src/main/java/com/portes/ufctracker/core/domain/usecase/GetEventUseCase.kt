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

class GetEventUseCase @Inject constructor(
    private val repository: EventsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<Int, EventModel>(dispatcher) {

    override fun execute(params: Int): Flow<Result<EventModel>> = repository.getEvent(params)
}