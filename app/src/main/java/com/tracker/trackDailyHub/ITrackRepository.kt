package com.tracker.trackDailyHub

import com.tracker.trackDailyHub.database.Category
import com.tracker.trackDailyHub.database.TimerData

interface ITrackRepository {
    suspend fun insertCategory(category: Category)
    suspend fun insertTrack(track: TimerData)
}