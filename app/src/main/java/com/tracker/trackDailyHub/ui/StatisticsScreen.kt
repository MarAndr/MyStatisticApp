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
import co.yml.charts.axis.DataCategoryOptions
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.Point
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarChartType
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
    val statisticScreenState by viewModel.statisticScreenState.collectAsState()

    val categories = statisticScreenState.categories
    val currentPeriod = statisticScreenState.selectedPeriod
    val totalTimeForEachCategory = statisticScreenState.totalTimeForEachCategory
    val isAllCategoriesSelected = statisticScreenState.isAllCategoriesSelected
    val selectedCategories = statisticScreenState.selectedCategories

    val timers = db.timerDao().getAllTimers()
    val timersState = timers.collectAsState(initial = listOf()).value
    val categoryToTotalTime = mutableMapOf<String, Long>()

    var totalTime by remember {
        mutableStateOf(0L)
    }

    var isDropDownPeriodShown by remember {
        mutableStateOf(false)
    }

    var isItemControlledByAllItem by remember {
        mutableStateOf(true)
    }

    timersState.forEach { timerData ->
        categoryToTotalTime[timerData.category.name] =
            categoryToTotalTime.getOrDefault(timerData.category.name, 0) + timerData.timeInSeconds
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
            if (selectedCategories.size == 1){
                SingleLineChartWithGridLines(
                    DataUtils.getLineChartData(
                        100,
                        start = 50,
                        maxRange = 100
                    )
                )
            } else{
                BarChart()
            }

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
                        viewModel.changeCategoryChecking(category, it)
                    }
                    )
                    Icon(
                        modifier = Modifier.padding(end = 4.dp),
                        painter = painterResource(id = category.iconResourceId),
                        contentDescription = "",
                        tint = Color.Unspecified
                    )
                    Text(text = "${category.name} - ${totalTimeForEachCategory[category]?:0L}")
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

@Composable
fun BarChart() {
    val stepSize = 5
    val barsData = DataUtils.getBarChartData(
        listSize = 8,
        maxRange = 8,
        barChartType = BarChartType.VERTICAL,
        dataCategoryOptions = DataCategoryOptions()
    )

    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .steps(barsData.size - 1)
        .bottomPadding(40.dp)
        .axisLabelAngle(20f)
        .labelData { index -> barsData[index].label }
        .build()

    val yAxisData = AxisData.Builder()
        .steps(stepSize)
        .labelAndAxisLinePadding(20.dp)
        .axisOffset(20.dp)
        .labelData { index -> (index * (100 / stepSize)).toString() }
        .build()

    val barChartData = BarChartData(
        chartData = barsData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        backgroundColor = MaterialTheme.colors.surface
    )
    
    BarChart(modifier = Modifier.height(350.dp), barChartData = barChartData)
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

fun getPointDataForSingleCategoryForMonth(category: Category): List<Point> {
    //по оси х - дни в месяце (30)
    //по оси y - значения замеров в минутах за каждый день месяца, по оси y будут минуты
    //мне нужно получить суммы замеров по конкретной категории за каждый день месяца(т.е список с 30 замерами)
    //потом пройтись по этому списку и добавить в список pointsData последовательно для каждого дня из 30 это значение
    val pointsData: List<Point> = listOf(
        Point(1f, 40f),
        Point(2f, 90f),
        Point(3f, 0f),
        Point(4f, 60f),
        Point(5f, 10f),
        Point(6f, 10f),
        Point(7f, 10f),
        Point(8f, 10f),
        Point(9f, 10f),
        Point(10f, 20f),
        Point(11f, 30f),
        Point(12f, 40f),
        Point(13f, 50f),
        Point(14f, 60f),
        Point(15f, 70f),
        Point(16f, 80f),
        Point(17f, 90f),
        Point(18f, 100f),
        Point(19f, 90f),
        Point(20f, 80f),
        Point(21f, 70f),
        Point(22f, 60f),
        Point(23f, 50f),
        Point(24f, 40f),
        Point(25f, 30f),
        Point(26f, 20f),
        Point(27f, 10f),
        Point(28f, 5f),
        Point(29f, 2f),
        Point(30f, 1f)
    )

    return pointsData
}