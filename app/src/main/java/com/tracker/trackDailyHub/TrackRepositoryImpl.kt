package com.tracker.trackDailyHub

import com.tracker.trackDailyHub.database.Category
import com.tracker.trackDailyHub.database.CategoryDao
import com.tracker.trackDailyHub.database.TimerDao
import com.tracker.trackDailyHub.database.TimerData
import javax.inject.Inject

class TrackRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val timerDao: TimerDao,
) : ITrackRepository {
    override suspend fun insertCategory(category: Category) {
        categoryDao.insertUniqueCategory(category)
    }

    override suspend fun insertTrack(track: TimerData) {
        timerDao.insertTimer(track)
    }
}