package com.portes.ufctracker.core.data.di

import com.google.firebase.firestore.FirebaseFirestore
import com.portes.ufctracker.core.data.datasources.EventsLocalDataSource
import com.portes.ufctracker.core.data.datasources.EventsLocalDataSourceImpl
import com.portes.ufctracker.core.data.datasources.EventsRemoteDataSource
import com.portes.ufctracker.core.data.datasources.EventsRemoteDataSourceImpl
import com.portes.ufctracker.core.data.datasources.FightersInfoLocalDataSource
import com.portes.ufctracker.core.data.datasources.FightersInfoLocalDataSourceImpl
import com.portes.ufctracker.core.data.datasources.FightersLocalDataSource
import com.portes.ufctracker.core.data.datasources.FightersLocalDataSourceImpl
import com.portes.ufctracker.core.data.datasources.FightsLocalDataSource
import com.portes.ufctracker.core.data.datasources.FightsLocalDataSourceImpl
import com.portes.ufctracker.core.data.repositories.EventsRepository
import com.portes.ufctracker.core.data.repositories.EventsRepositoryImpl
import com.portes.ufctracker.core.data.repositories.FightBetsRepository
import com.portes.ufctracker.core.data.repositories.FightBetsRepositoryImpl
import com.portes.ufctracker.core.data.repositories.FightersInfoRepository
import com.portes.ufctracker.core.data.repositories.FightersInfoRepositoryImpl
import com.portes.ufctracker.core.data.repositories.FightersRepository
import com.portes.ufctracker.core.data.repositories.FightersRepositoryImpl
import com.portes.ufctracker.core.data.repositories.FightsRepository
import com.portes.ufctracker.core.data.repositories.FightsRepositoryImpl
import com.portes.ufctracker.core.data.repositories.PreferencesRepository
import com.portes.ufctracker.core.data.repositories.PreferencesRepositoryImpl
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

        @Binds
        fun bindsFightBetsRepository(
            fightBetsRepository: FightBetsRepositoryImpl,
        ): FightBetsRepository

        @Binds
        fun bindsFightersLocalDataSource(
            fightersLocalDataSource: FightersLocalDataSourceImpl,
        ): FightersLocalDataSource

        @Binds
        fun bindsFightersRepository(
            fightersRepository: FightersRepositoryImpl,
        ): FightersRepository

        @Binds
        fun bindsPreferencesRepository(
            preferencesRepository: PreferencesRepositoryImpl,
        ): PreferencesRepository

        @Binds
        fun bindsFightersInfoLocalDataSource(
            fightersInfoLocalDataSource: FightersInfoLocalDataSourceImpl,
        ): FightersInfoLocalDataSource

        @Binds
        fun bindsFightsLocalDataSource(
            fightsLocalDataSource: FightsLocalDataSourceImpl,
        ): FightsLocalDataSource

        @Binds
        fun bindsFightsRepository(
            fightsRepository: FightsRepositoryImpl,
        ): FightsRepository

        @Binds
        fun bindsFightersInfoRepository(
            fightersInfoRepository: FightersInfoRepositoryImpl,
        ): FightersInfoRepository
    }

    @Singleton
    @Provides
    fun providesTopTenService(retrofit: Retrofit): Services =
        retrofit.create(Services::class.java)

    @Provides
    fun providesFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}
