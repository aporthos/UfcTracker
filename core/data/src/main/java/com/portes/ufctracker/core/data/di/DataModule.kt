package com.portes.ufctracker.core.data.di

import com.portes.ufctracker.core.data.datasources.EventsLocalDataSource
import com.portes.ufctracker.core.data.datasources.EventsLocalDataSourceImpl
import com.portes.ufctracker.core.data.datasources.EventsRemoteDataSource
import com.portes.ufctracker.core.data.datasources.EventsRemoteDataSourceImpl
import com.portes.ufctracker.core.data.repositories.EventsRepository
import com.portes.ufctracker.core.data.repositories.EventsRepositoryImpl
import com.portes.ufctracker.core.data.services.Services
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Module
    @InstallIn(SingletonComponent::class)
    internal interface DataModuleBinder {
        @Binds
        fun bindsEventsRepository(
            eventsRepository: EventsRepositoryImpl,
        ): EventsRepository

        @Binds
        fun bindsEventsLocalDataSource(
            eventsLocalDataSource: EventsLocalDataSourceImpl,
        ): EventsLocalDataSource

        @Binds
        fun bindsEventsRemoteDataSource(
            eventsRemoteDataSource: EventsRemoteDataSourceImpl,
        ): EventsRemoteDataSource
    }

    @Singleton
    @Provides
    fun providesTopTenService(retrofit: Retrofit): Services =
        retrofit.create(Services::class.java)

}