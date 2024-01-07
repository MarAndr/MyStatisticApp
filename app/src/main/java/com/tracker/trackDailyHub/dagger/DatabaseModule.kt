package com.tracker.trackDailyHub.dagger

import android.content.Context
import android.content.res.Resources
import androidx.room.Room
import com.tracker.trackDailyHub.database.AppDatabase
import com.tracker.trackDailyHub.database.CategoryDao
import com.tracker.trackDailyHub.database.TimerDao
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
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "timer-database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @Provides
    @Singleton
    fun provideCategoryDao(appDatabase: AppDatabase): CategoryDao {
        return appDatabase.categoryDao()
    }


    @Provides
    @Singleton
    fun provideTrackDao(appDatabase: AppDatabase): TimerDao {
        return appDatabase.timerDao()
    }
}