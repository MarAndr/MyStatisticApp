package com.tracker.trackDailyHub

import com.tracker.trackDailyHub.database.TimerData

data class AddMeasurementScreenState(
    val categoriesNames: List<String>,
    val tracks: List<TimerData>,
)