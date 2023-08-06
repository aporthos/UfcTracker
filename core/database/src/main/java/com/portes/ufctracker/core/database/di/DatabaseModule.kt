package com.portes.ufctracker.core.database.di

import android.content.Context
import androidx.room.Room
import com.portes.ufctracker.core.database.dao.FightersDao
import com.portes.ufctracker.core.database.dao.FightsDao
import com.portes.ufctracker.core.database.dao.FightersInfoDao
import com.portes.ufctracker.core.database.UfcTrackerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesUfcTrackerDatabase(
        @ApplicationContext context: Context,
    ): UfcTrackerDatabase = Room.databaseBuilder(
        context = context, UfcTrackerDatabase::class.java,
        "UfcTrackerDatabase",
    )
        // TODO: Restore for version final
        //.createFromAsset("UfcTrackerDatabase.db")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun providesFighterInfoDao(
        database: UfcTrackerDatabase,
    ): FightersInfoDao = database.fighterInfoDao()

    @Provides
    @Singleton
    fun providesFightsDao(
        database: UfcTrackerDatabase,
    ): FightsDao = database.fightDao()

    @Provides
    @Singleton
    fun providesFightersDao(
        database: UfcTrackerDatabase,
    ): FightersDao = database.fightersDao()
}
