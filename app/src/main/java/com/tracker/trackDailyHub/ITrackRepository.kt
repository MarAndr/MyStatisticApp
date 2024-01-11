package com.tracker.trackDailyHub

import com.tracker.trackDailyHub.database.Category
import com.tracker.trackDailyHub.database.TimerData
import kotlinx.coroutines.flow.Flow

interface ITrackRepository {
    suspend fun insertCategory(category: Category)
    suspend fun insertDefaultCategories()
    suspend fun getCategories(): Flow<List<Category>>
    suspend fun insertTrack(track: TimerData)
    suspend fun getTotalTimeForCategoryAllTime(category: Category): Flow<Long?>
    suspend fun getTotalTimeForCategoryToday(category: Category): Flow<Long?>
    suspend fun getTotalTimeForCategoryThisWeek(category: Category): Flow<Long?>
    suspend fun getTotalTimeForCategoryThisMonth(category: Category): Flow<Long?>
    suspend fun getTotalTimeForCategoryThisYear(category: Category): Flow<Long?>
    suspend fun isCategoryNameUnique(categoryName: String): Boolean
}