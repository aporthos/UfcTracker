package com.portes.ufctracker.core.data.datasources

import android.content.Context
import com.portes.ufctracker.core.common.models.Result
import com.portes.ufctracker.core.data.convertToList
import com.portes.ufctracker.core.data.convertToObject
import com.portes.ufctracker.core.model.entities.EventEntity
import com.portes.ufctracker.core.model.entities.toModel
import com.portes.ufctracker.core.model.models.EventModel
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import timber.log.Timber
import java.io.FileNotFoundException
import javax.inject.Inject

class EventsLocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val moshi: Moshi,
) : EventsLocalDataSource {
    companion object {
        private const val EVENTS_JSON = "events.json"
        private const val EVENTS_BY_ID_JSON = "eventById.json"
    }

    override fun getEventsList(): Flow<Result<List<EventModel>>> {
        val response = try {
            val result = context.assets.convertToList<EventEntity>(EVENTS_JSON, moshi)
            result?.map {
                it.toModel()
            }?.let {
                Result.Success(it)
            } ?: run {
                Result.Error("Not read file")
            }
        } catch (e: FileNotFoundException) {
            Timber.e(e)
            Result.Error(e.localizedMessage)
        }

        return flowOf(response)
    }

    override fun getEvent(eventId: Int): Flow<Result<EventModel>> {
        val response = try {
            val result = context.assets.convertToObject<EventEntity>(EVENTS_BY_ID_JSON, moshi)
            result?.toModel()?.let {
                Result.Success(it)
            } ?: run {
                Result.Error("Not read file")
            }
        } catch (e: FileNotFoundException) {
            Timber.e(e)
            Result.Error(e.localizedMessage)
        }

        return flowOf(response)
    }
}

interface EventsLocalDataSource {
    fun getEventsList(): Flow<Result<List<EventModel>>>
    fun getEvent(eventId: Int): Flow<Result<EventModel>>
}