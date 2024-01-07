package com.tracker.trackDailyHub

import com.tracker.trackDailyHub.database.Category
import com.tracker.trackDailyHub.database.TimerData

data class AddMeasurementScreenState(
    val categories: List<Category>,
    val tracks: List<TimerData>,
)