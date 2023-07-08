package com.portes.ufctracker.core.data.datasources

import com.portes.ufctracker.core.common.di.IoDispatcher
import com.portes.ufctracker.core.common.models.Result
import com.portes.ufctracker.core.common.models.callApi
import com.portes.ufctracker.core.data.services.Services
import com.portes.ufctracker.core.model.entities.toModel
import com.portes.ufctracker.core.model.models.EventModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class EventsRemoteDataSourceImpl @Inject constructor(
    private val services: Services,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : EventsRemoteDataSource {
    override fun getEventsList(): Flow<Result<List<EventModel>>> = flow {
        val events = services.getEventsList().callApi {
            it.map { it.toModel() }
        }
        emit(events)
    }.catch {
        emit(Result.Error(it.message))
    }.flowOn(dispatcher)

    override fun getEvent(eventId: Int): Flow<Result<EventModel>> = flow {
        val events = services.getEvent(eventId).callApi {
            it.toModel()
        }
        emit(events)
    }.catch {
        emit(Result.Error(it.message))
    }.flowOn(dispatcher)
}

interface EventsRemoteDataSource {
    fun getEventsList(): Flow<Result<List<EventModel>>>
    fun getEvent(eventId: Int): Flow<Result<EventModel>>
}
