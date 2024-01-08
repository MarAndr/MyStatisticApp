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

enum class StatisticPeriod{
    DAY,
    WEEK,
    MONTH,
    YEAR,
}

enum class CHART{
    BAR,
    PIE,
    LINE,
}