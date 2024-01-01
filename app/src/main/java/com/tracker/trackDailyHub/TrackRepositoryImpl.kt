package com.tracker.trackDailyHub

import com.tracker.trackDailyHub.database.Category
import com.tracker.trackDailyHub.database.TimerData

class TrackRepositoryImpl constructor(
//    private val categoryDao: CategoryDao,
//    private val timerDao: TrackDao,
) : ITrackRepository {
    override suspend fun insertCategory(category: Category) {
//        categoryDao.insertCategory(category)
    }

    override suspend fun insertTrack(track: TimerData) {
//        timerDao.insertTrack(track)
    }
}