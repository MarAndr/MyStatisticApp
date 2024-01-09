package com.tracker.trackDailyHub.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


// Room Database setup
@Database(entities = [TimerData::class, Category::class], version = 1)
@TypeConverters(CategoryConverter::class, LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun timerDao(): TimerDao
    abstract fun categoryDao(): CategoryDao
}