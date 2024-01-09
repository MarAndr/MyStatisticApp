package com.tracker.trackDailyHub

import com.tracker.trackDailyHub.database.Category

data class StatisticScreenState(
    val categories: List<Category>,
    val selectedCategories: List<Category>,
    val selectedPeriod: StatisticPeriod,
    val isAllCategoriesSelected: Boolean,
    val selectedChart: CHART,
    val totalTime: Long = 0L,
    val averageDayTime: Long = 0L,
)

enum class StatisticPeriod(val text: String){
    DAY(text = "Day"),
    WEEK(text = "Week"),
    MONTH(text = "Month"),
    YEAR(text = "Year"),
}

enum class CHART{
    BAR,
    PIE,
    LINE,
}