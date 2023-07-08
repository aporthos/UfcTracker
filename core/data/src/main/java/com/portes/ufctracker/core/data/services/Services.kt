package com.portes.ufctracker.core.data.services

import com.portes.ufctracker.core.model.entities.EventEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface Services {
    @GET("/v3/mma/scores/json/Schedule/UFC/2023?key=ff09fe226cdf48d8a7a3b148277bbc5c")
    suspend fun getEventsList(): Response<List<EventEntity>>

    @GET("v3/mma/scores/json/Event/{eventId}?key=ff09fe226cdf48d8a7a3b148277bbc5c")
    suspend fun getEvent(@Path("eventId") eventId: Int): Response<EventEntity>
}
