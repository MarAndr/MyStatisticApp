package com.tracker.trackDailyHub.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class TimerData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: Category,
    val timeInSeconds: Long,
    val date: LocalDateTime = LocalDateTime.now(),
)
