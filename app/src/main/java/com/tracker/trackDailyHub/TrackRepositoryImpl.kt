package com.tracker.trackDailyHub

import com.tracker.trackDailyHub.database.Category
import com.tracker.trackDailyHub.database.CategoryDao
import com.tracker.trackDailyHub.database.TimerDao
import com.tracker.trackDailyHub.database.TimerData
import kotlinx.coroutines.flow.Flow
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

    override suspend fun getCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories()
    }
}