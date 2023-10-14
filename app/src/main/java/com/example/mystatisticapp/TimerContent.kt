package com.example.mystatisticapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TimerContent(
    currentTime: Long,
    isRunning: Boolean,
    onTimerStart: () -> Unit,
    onTimerStop: () -> Unit,
    onTimerPause: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display time or "Timer Stopped"
        // Button to start or pause the timer
        // Button to stop the timer and show the snackbar
        // Implement UI components here
    }
}
