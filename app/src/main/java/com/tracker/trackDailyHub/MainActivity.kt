package com.tracker.trackDailyHub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tracker.trackDailyHub.ui.theme.MyStatisticAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyStatisticAppTheme {
                TrackDailyHub()
            }
        }
    }
}
