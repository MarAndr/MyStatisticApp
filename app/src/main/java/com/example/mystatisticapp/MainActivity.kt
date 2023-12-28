package com.example.mystatisticapp

import MainScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.example.mystatisticapp.database.AppDatabase
import com.example.mystatisticapp.ui.theme.MyStatisticAppTheme


class MainActivity : ComponentActivity() {
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "timer-database"
        ).build()

        setContent {
            MyStatisticAppTheme {
                MainScreen(db = db)
            }
        }
    }
}
