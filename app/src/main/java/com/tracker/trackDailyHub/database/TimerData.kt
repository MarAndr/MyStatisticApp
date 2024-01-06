package com.tracker.trackDailyHub.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TimerData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: Category,
    val timeInSeconds: Long,
)
