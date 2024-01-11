package com.tracker.trackDailyHub.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
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
import com.tracker.trackDailyHub.StatisticPeriod
import com.tracker.trackDailyHub.StatisticViewModel
import com.tracker.trackDailyHub.database.AppDatabase
import com.tracker.trackDailyHub.database.Category
import com.tracker.trackdailyhub.R


@Composable
fun StatisticScreen(
    viewModel: StatisticViewModel,
    navController: NavHostController,
    db: AppDatabase,
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

    var currentPeriod by remember {
        mutableStateOf(StatisticPeriod.DAY)
    }

    var isDropDownPeriodShown by remember {
        mutableStateOf(false)
    }

    var totalTimeForEachCategory by remember {
        mutableStateOf(emptyMap<Category, Long>())
    }

    var isAllCategoriesSelected by remember {
        mutableStateOf(true)
    }

    var selectedCategories by remember {
        mutableStateOf<List<Category>>(emptyList())
    }

    var isItemControlledByAllItem by remember {
        mutableStateOf(true)
    }

    timersState.forEach { timerData ->
        categoryToTotalTime[timerData.category.name] =
            categoryToTotalTime.getOrDefault(timerData.category.name, 0) + timerData.timeInSeconds
    }

    LaunchedEffect(viewModel.statisticScreenState) {
        viewModel.loadCategories()
        viewModel.statisticScreenState.collect {
            categories = it.categories
            currentPeriod = it.selectedPeriod
            totalTimeForEachCategory = it.totalTimeForEachCategory
            isAllCategoriesSelected = it.isAllCategoriesSelected
            selectedCategories = it.selectedCategories
        }
    }

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
        Row(Modifier.padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = {
                isDropDownPeriodShown = !isDropDownPeriodShown
            }) {
                Text(text = currentPeriod.text)
                DropDownMenu(
                    isDropDownShown = isDropDownPeriodShown,
                    onDismissRequest = {
                        isDropDownPeriodShown = false
                    },
                    viewModel = viewModel,
                    onItemClick = {
                        isDropDownPeriodShown = false
                    }
                )
            }
        }
        Box(Modifier.padding(horizontal = 16.dp)) {
            SingleLineChartWithGridLines(
                DataUtils.getLineChartData(
                    100,
                    start = 50,
                    maxRange = 100
                )
            )
        }

        LazyColumn(Modifier.padding(horizontal = 16.dp), content = {
            item {
                Row(Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isAllCategoriesSelected, onCheckedChange = {
                        viewModel.switchTheAllCategories(!it)
                        isItemControlledByAllItem = true
                    })
                    Text(text = "All categories")
                }
            }
            items(categories) { category ->

                val isCategoryChecked = if (isItemControlledByAllItem) {
                    isAllCategoriesSelected
                } else {
                    selectedCategories.any {
                        it == category
                    }
                }

                Row(Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isCategoryChecked, onCheckedChange = {
                        isItemControlledByAllItem = false
                        isAllCategoriesSelected = false
                        viewModel.changeCategoryChecking(category, it)
                    }
                    )
                    Icon(
                        modifier = Modifier.padding(end = 4.dp),
                        painter = painterResource(id = category.iconResourceId),
                        contentDescription = "",
                        tint = Color.Unspecified
                    )
                    Text(text = "${category.name} - ${totalTimeForEachCategory[category]}")
                }
            }
        })
    }
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

fun formatTime(seconds: Long): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return if (minutes > 0) {
        "$minutes min"
    } else {
        "$remainingSeconds sec"
    }
}

@Composable
fun DropDownMenu(
    isDropDownShown: Boolean,
    onDismissRequest: () -> Unit,
    viewModel: StatisticViewModel,
    onItemClick: () -> Unit,
) {
    DropdownMenu(
        expanded = isDropDownShown,
        onDismissRequest = onDismissRequest,
    ) {
        StatisticPeriod.entries.forEach { period ->
            DropdownMenuItem(text = { Text(text = period.text) }, onClick = {
                viewModel.setPeriod(period)
                onItemClick()
            })
        }
    }
}