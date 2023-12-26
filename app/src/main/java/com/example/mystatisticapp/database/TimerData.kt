package com.example.mystatisticapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TimerData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String,
    val timeInSeconds: Long
)
