package com.example.mystatisticapp

import androidx.room.Database
import androidx.room.RoomDatabase


// Room Database setup
@Database(entities = [TimerData::class, Category::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun timerDao(): TimerDao
    abstract fun categoryDao(): CategoryDao
}