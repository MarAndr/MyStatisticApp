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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tracker.trackDailyHub.ui.NavigationCustomButtons
import com.tracker.trackDailyHub.ui.theme.Gray100
import com.tracker.trackDailyHub.ui.theme.Gray800
import com.tracker.trackDailyHub.ui.theme.Green800
import com.tracker.trackDailyHub.ui.theme.MyStatisticAppTheme
import com.tracker.trackDailyHub.ui.theme.Red
import com.tracker.trackdailyhub.R

@Composable
fun StartScreen(
    navController: NavHostController? = null,
    viewModel: StartScreenViewModel,
) {
    var time by remember {
        mutableLongStateOf(0L)
    }

    var timerState by remember { mutableStateOf<TimerState>(TimerState.INITIAL) }

    LaunchedEffect(viewModel.startScreenState) {
        viewModel.startScreenState.collect { state ->
            time = state.time
            timerState = state.timerState
        }
    }

    StartScreen(time, timerState, viewModel, navController)
}

@Composable
private fun StartScreen(
    time: Long,
    timerState: TimerState,
    viewModel: StartScreenViewModel? = null,
    navController: NavHostController? = null,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = formatTime(time),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(16.dp)
        )

        SetControlButtons(
            timerState = timerState,
            onStartButtonClick = { viewModel?.startTimer() },
            onPauseButtonClick = { viewModel?.pauseTimer() },
            onResumeButtonClick = { viewModel?.startTimer() },
            onStopButtonClick = {
                viewModel?.pauseTimer()
                navController?.navigate(TrackDailyHubDestination.AddSurveyScreen.withArgs(time))
            },
        )

        Spacer(modifier = Modifier.weight(1f))

        NavigationCustomButtons(
            modifier = Modifier
                .background(Gray100, shape = RoundedCornerShape(100)),
            onStatisticClick = {
                navController?.navigate(TrackDailyHubDestination.StatisticScreen.route)
            },
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
            CustomButton(
                horizontalPadding = 38.dp,
                drawableId = R.drawable.solar_play_bold,
                isTextNeed = true
            ) {
                onStartButtonClick()
            }
        }

        TimerState.RUNNING -> {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                CustomButton(
                    drawableId = R.drawable.ic_round_pause,
                    backgroundColor = Color.White,
                    contentColor = Gray800
                ) {
                    onPauseButtonClick()
                }

                CustomButton(drawableId = R.drawable.ic_round_stop, backgroundColor = Red) {
                    onStopButtonClick()
                }
            }
        }

        TimerState.PAUSED -> {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
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

fun formatTime(currentTime: Long): String {
    val hours = currentTime / 3600
    val minutes = (currentTime % 3600) / 60
    val seconds = currentTime % 60

    return "%02d:%02d:%02d".format(hours, minutes, seconds)
}


@Preview(showBackground = true)
@Composable
private fun StartScreenPreview() = MyStatisticAppTheme {
    StartScreen(time = 10, timerState = TimerState.PAUSED, navController = null)
}

