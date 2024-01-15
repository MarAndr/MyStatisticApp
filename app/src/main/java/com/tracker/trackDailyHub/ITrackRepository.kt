package com.tracker.trackDailyHub

import com.tracker.trackDailyHub.database.Category
import com.tracker.trackDailyHub.database.DayTotalTime
import com.tracker.trackDailyHub.database.TimerData
import kotlinx.coroutines.flow.Flow

interface ITrackRepository {
    suspend fun insertCategory(category: Category)
    suspend fun insertDefaultCategories()
    fun getCategoriesFlow(): Flow<List<Category>>
    suspend fun getCategories(): List<Category>
    suspend fun insertTrack(track: TimerData)
    suspend fun getTotalTimeForCategoryAllTime(category: Category): Long?
    suspend fun getTotalTimeForCategoryToday(category: Category): Long?
    suspend fun getTotalTimeForCategoryThisWeek(category: Category): Long?
    suspend fun getTotalTimeForCategoryThisMonth(category: Category): Long?
    suspend fun getTotalTimeForCategoryThisYear(category: Category): Long?
    suspend fun isCategoryNameUnique(categoryName: String): Boolean
    suspend fun getTotalTimeForCategoryLast30Days(category: Category): List<DayTotalTime>
}