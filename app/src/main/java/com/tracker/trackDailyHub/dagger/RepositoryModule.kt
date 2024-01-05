package com.tracker.trackDailyHub.dagger

import com.tracker.trackDailyHub.ITrackRepository
import com.tracker.trackDailyHub.TrackRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun provideTrackRepository(
        impl: TrackRepositoryImpl
    ): ITrackRepository
}