package com.tracker.trackDailyHub

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tracker.trackDailyHub.ui.NavigationCustomButtons
import com.tracker.trackDailyHub.ui.theme.Gray100
import com.tracker.trackDailyHub.ui.theme.Gray800
import com.tracker.trackDailyHub.ui.theme.Green800
import com.tracker.trackDailyHub.ui.theme.Red
import com.tracker.trackdailyhub.R

@Composable
fun StartScreen(
    time: String,
    timerState: TimerState,
    onStartButtonClick: () -> Unit,
    onStopButtonClick: () -> Unit,
    onPauseButtonClick: () -> Unit,
    onResumeButtonClick: () -> Unit,
    onStatisticClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

        Spacer(modifier = Modifier.weight(1f))
            Text(
                text = time,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(16.dp)
            )

            SetControlButtons(
                timerState = timerState,
                onStartButtonClick = onStartButtonClick,
                onPauseButtonClick = onPauseButtonClick,
                onResumeButtonClick = onResumeButtonClick,
                onStopButtonClick = onStopButtonClick,
            )

        Spacer(modifier = Modifier.weight(1f))

        NavigationCustomButtons(
            modifier = Modifier
                .background(Gray100, shape = RoundedCornerShape(100)),
            onStatisticClick = onStatisticClick,
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun SetControlButtons(
    timerState: TimerState,
    onStartButtonClick: () -> Unit,
    onPauseButtonClick: () -> Unit,
    onStopButtonClick: () -> Unit,
    onResumeButtonClick: () -> Unit,
) {
    when (timerState) {
        TimerState.INITIAL -> {
            CustomButton(horizontalPadding = 32.dp, drawableId = R.drawable.solar_play_bold, isTextNeed = true) {
                onStartButtonClick()
            }
        }

        TimerState.RUNNING -> {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                CustomButton(drawableId = R.drawable.ic_round_pause, backgroundColor = Color.White, contentColor = Gray800) {
                    onPauseButtonClick()
                }

                CustomButton(drawableId = R.drawable.ic_round_stop, backgroundColor = Red) {
                    onStopButtonClick()
                }
            }
        }

        TimerState.PAUSED -> {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                CustomButton(drawableId = R.drawable.solar_play_bold) {
                    onResumeButtonClick()
                }

                CustomButton(drawableId = R.drawable.ic_round_stop, backgroundColor = Red) {
                    onStopButtonClick()
                }
            }
        }
    }
}

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Green800,
    contentColor: Color = Color.White,
    horizontalPadding: Dp = 16.dp,
    @DrawableRes drawableId: Int,
    isTextNeed: Boolean = false,
    onClick: () -> Unit,
) {
    Button(
        contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = 16.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.shadow(elevation = 3.dp, shape = RoundedCornerShape(16.dp)),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor,
        ),
    ) {
        Icon(
            painter = painterResource(id = drawableId),
            contentDescription = ""
        )
        if (isTextNeed) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Start", style = MaterialTheme.typography.button)
        }
    }
}


