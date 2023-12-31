package com.example.mystatisticapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

@Composable
fun StartScreen(
    time: String,
    isRunning: Boolean,
    onStartButtonClick: () -> Unit,
    onStopButtonClick: () -> Unit,
    onPauseButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        androidx.compose.material.Text(
            fontFamily = FontFamily.Monospace,
            text = time,
            style = MaterialTheme.typography.h2,
            modifier = Modifier.padding(16.dp)
        )

        if (!isRunning) {
            androidx.compose.material.Button(
                onClick = { onStartButtonClick() },
            ) {
                androidx.compose.material.Text(text = "Start")
            }
        } else {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                androidx.compose.material.Button(
                    onClick = { onStopButtonClick() }
                ) {
                    androidx.compose.material.Text(text = "Stop")
                }

                androidx.compose.material.Button(onClick = { onPauseButtonClick() }) {
                    androidx.compose.material.Text(text = "Pause")
                }
            }
        }
    }
}