package com.tracker.trackDailyHub.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerDao {
    @Query("SELECT * FROM TimerData")
    fun getAllTimers(): Flow<List<TimerData>>

    @Query("SELECT SUM(timeInSeconds) FROM TimerData WHERE category = :category")
    suspend fun getTotalTimeForCategoryAllTime(category: Category): Long?

    @Query("SELECT SUM(timeInSeconds) FROM TimerData WHERE category = :category AND strftime('%Y-%m-%d', date) = strftime('%Y-%m-%d', 'now')")
    suspend fun getTotalTimeForCategoryToday(category: Category): Long?

    @Query("SELECT SUM(timeInSeconds) FROM TimerData WHERE category = :category AND strftime('%Y-%m', date) = strftime('%Y-%m', 'now')")
    suspend fun getTotalTimeForCategoryThisMonth(category: Category): Long?

    @Query("SELECT SUM(timeInSeconds) FROM TimerData WHERE category = :category AND strftime('%Y-%m-%d', date) BETWEEN date('now', 'weekday 0', '-6 days') AND date('now', 'weekday 0', '0 days')")
    suspend fun getTotalTimeForCategoryThisWeek(category: Category): Long?

    @Query("SELECT SUM(timeInSeconds) FROM TimerData WHERE category = :category AND strftime('%Y', date) = strftime('%Y', 'now')")
    suspend fun getTotalTimeForCategoryThisYear(category: Category): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimer(timer: TimerData)
}