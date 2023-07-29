package com.portes.ufctracker.core.data.repositories

import com.portes.ufctracker.core.common.models.Result
import com.portes.ufctracker.core.data.BuildConfig
import com.portes.ufctracker.core.data.datasources.EventsLocalDataSource
import com.portes.ufctracker.core.data.datasources.EventsRemoteDataSource
import com.portes.ufctracker.core.model.models.EventModel
import com.portes.ufctracker.core.model.models.FightModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(
    private val eventsLocalDataSource: EventsLocalDataSource,
    private val eventsRemoteDataSource: EventsRemoteDataSource,
) : EventsRepository {

    override fun getEventsList(): Flow<Result<List<EventModel>>> {
        return if (BuildConfig.READ_LOCAL) {
            eventsLocalDataSource.getEventsList()
        } else {
            eventsRemoteDataSource.getEventsList()
        }
    }

    override fun getFightsList(eventId: Int): Flow<Result<List<FightModel>>> {
        return if (BuildConfig.READ_LOCAL) {
            eventsLocalDataSource.getFightsList(eventId)
        } else {
            eventsRemoteDataSource.getFightsList(eventId)
        }
    }
}

interface EventsRepository {
    fun getEventsList(): Flow<Result<List<EventModel>>>
    fun getFightsList(eventId: Int): Flow<Result<List<FightModel>>>
}
