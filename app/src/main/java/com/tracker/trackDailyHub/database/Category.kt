package com.tracker.trackDailyHub.database

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var name: String = "",
    val iconResourceId: Int = 0,
    val color: Int = Color.Unspecified.toArgb()
)
