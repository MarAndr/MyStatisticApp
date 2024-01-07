package com.tracker.trackDailyHub

import com.tracker.trackDailyHub.database.Category
import com.tracker.trackDailyHub.database.TimerData
import kotlinx.coroutines.flow.Flow

interface ITrackRepository {
    suspend fun insertCategory(category: Category)
    suspend fun insertDefaultCategories()
    suspend fun getCategories(): Flow<List<Category>>
    suspend fun insertTrack(track: TimerData)
}