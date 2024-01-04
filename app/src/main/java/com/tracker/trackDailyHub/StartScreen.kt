package com.tracker.trackDailyHub

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tracker.trackDailyHub.ui.NavigationCustomButtons
import com.tracker.trackDailyHub.ui.theme.Gray100
import com.tracker.trackdailyhub.R

@Composable
fun StartScreen(
    time: String,
    isRunning: Boolean,
    onStartButtonClick: () -> Unit,
    onStopButtonClick: () -> Unit,
    onPauseButtonClick: () -> Unit,
    onStatisticClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = time,
                style = MaterialTheme.typography.h1,
                modifier = Modifier.padding(16.dp)
            )

            if (!isRunning) {
                Button(
                    onClick = { onStartButtonClick() },
                ) {
                    Text(
                        text = stringResource(id = R.string.startScreen_start_button),
                        style = MaterialTheme.typography.h1
                    )
                }
            } else {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onStopButtonClick() }
                    ) {
                        Text(
                            text = stringResource(id = R.string.startScreen_stop_button),
                            style = MaterialTheme.typography.h1
                        )
                    }

                    Button(onClick = { onPauseButtonClick() }) {
                        Text(
                            text = stringResource(id = R.string.startScreen_pause_button),
                            style = MaterialTheme.typography.h1
                        )
                    }
                }
            }
        }
        NavigationCustomButtons(
            modifier = Modifier
                .background(Gray100, shape = RoundedCornerShape(100)),
            onStatisticClick = onStatisticClick,
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}