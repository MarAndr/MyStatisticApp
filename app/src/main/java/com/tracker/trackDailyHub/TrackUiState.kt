package com.tracker.trackDailyHub

import com.tracker.trackDailyHub.database.TimerData

data class TrackUiState(
    val categories: List<String>,
    val tracks: List<TimerData>
)