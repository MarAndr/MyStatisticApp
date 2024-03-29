package com.tracker.trackDailyHub.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.Point
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.tracker.trackDailyHub.StatisticViewModel
import com.tracker.trackDailyHub.database.AppDatabase
import com.tracker.trackDailyHub.database.Category
import com.tracker.trackdailyhub.R


@Composable
fun StatisticScreen(
    viewModel: StatisticViewModel,
    navController: NavHostController,
    db: AppDatabase
) {
    val timers = db.timerDao().getAllTimers()
    val timersState = timers.collectAsState(initial = listOf()).value
    val categoryToTotalTime = mutableMapOf<String, Long>()

    var categories by remember {
        mutableStateOf(listOf<Category>())
    }

    var totalTime by remember {
        mutableStateOf(0L)
    }

    timersState.forEach { timerData ->
        categoryToTotalTime[timerData.category.name] =
            categoryToTotalTime.getOrDefault(timerData.category.name, 0) + timerData.timeInSeconds
    }

    LaunchedEffect(viewModel.statisticScreenState){
        viewModel.statisticScreenState.collect{
            categories = it.categories
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.statistics_screen_title),
                        style = MaterialTheme.typography.h2,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                backgroundColor = Color.White,
                elevation = AppBarDefaults.TopAppBarElevation
            )

            Spacer(modifier = Modifier.height(32.dp))
            Row {
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {}) {
                    Text(text = "Period")
                }
            }
            SingleLineChartWithGridLines(
                DataUtils.getLineChartData(
                    100,
                    start = 50,
                    maxRange = 100
                )
            )
            LazyColumn(content = {
                item {
                    Row(Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = true, onCheckedChange = {})
                        Text(text = "All categories")
                    }
                }
                items(categories){ category ->
                    Row(Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = true, onCheckedChange = {})
                        Icon(modifier = Modifier.padding(end = 4.dp), painter = painterResource(id = category.iconResourceId), contentDescription = "", tint = Color.Unspecified)
                        Text(text = "${category.name} - $totalTime")
                    }
                }
            })

        }
    }

//    SectionList(sections = categoryToTotalTime)

}

@Composable
fun SingleLineChartWithGridLines(pointsData: List<Point>) {
    val steps = 5
    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .topPadding(105.dp)
        .steps(pointsData.size - 1)
        .labelData { i -> pointsData[i].x.toInt().toString() }
        .labelAndAxisLinePadding(15.dp)
        .build()
    val yAxisData = AxisData.Builder()
        .steps(steps)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            // Add yMin to get the negative axis values to the scale
            val yMin = pointsData.minOf { it.y }
            val yMax = pointsData.maxOf { it.y }
            val yScale = (yMax - yMin) / steps
            ((i * yScale) + yMin).formatToSinglePrecision()
        }.build()
    val data = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    LineStyle(),
                    IntersectionPoint(),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(),
                    SelectionHighlightPopUp()
                )
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines()
    )
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        lineChartData = data
    )

}


@Composable
fun SectionList(sections: Map<String, Long>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(sections.toList()) { (category, timeInSeconds) ->
            SectionItem(category, timeInSeconds)
        }
    }
}

@Composable
fun SectionItem(category: String, duration: Long) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = "Название раздела: $category")
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Продолжительность: ${formatTime(duration)}")
    }
}

fun formatTime(seconds: Long): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return if (minutes > 0) {
        "$minutes min"
    } else {
        "$remainingSeconds sec"
    }
}