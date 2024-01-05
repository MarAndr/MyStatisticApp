package com.tracker.trackDailyHub.dagger

import com.tracker.trackDailyHub.ITrackRepository
import com.tracker.trackDailyHub.TrackDailyViewModel
import com.tracker.trackDailyHub.TrackRepositoryImpl
import com.tracker.trackDailyHub.database.CategoryDao
import com.tracker.trackDailyHub.database.TimerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideTrackRepository(
        categoryDao: CategoryDao,
        trackDao: TimerDao
    ): ITrackRepository {
        return TrackRepositoryImpl(categoryDao, trackDao)
    }

    @Provides
    fun provideTrackDailyViewModel(
        trackRepository: ITrackRepository
    ): TrackDailyViewModel {
        return TrackDailyViewModel(trackRepository)
    }
}