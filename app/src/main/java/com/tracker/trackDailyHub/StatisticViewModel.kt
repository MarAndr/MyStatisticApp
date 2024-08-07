package com.tracker.trackDailyHub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tracker.trackDailyHub.database.Category
import com.tracker.trackDailyHub.database.DayTotalTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val trackRepository: ITrackRepository
) : ViewModel() {

    private val period = MutableStateFlow(StatisticPeriod.DAY)
    private val selectedCategories = MutableStateFlow(emptyList<Category>())

    init {
        viewModelScope.launch {
            selectedCategories.value = trackRepository.getCategories()
        }
    }

    val statisticScreenState: StateFlow<StatisticScreenState> = combine(
        period,
        selectedCategories,
        trackRepository.getCategoriesFlow(),
    ) { period, selectedCategories, categories ->
        StatisticScreenState(
            categories = categories,
            selectedCategories = selectedCategories,
            selectedPeriod = period,
            isAllCategoriesSelected = categories == selectedCategories,
            selectedChart = CHART.BAR,
            totalTimeForEachCategory = countTotalTime(categories, period),
            totalTimeForCategoryLast30Days = getTotalTimeForCategoryLast30Days()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = StatisticScreenState(
            categories = emptyList(),
            selectedCategories = emptyList(),
            selectedPeriod = StatisticPeriod.DAY,
            isAllCategoriesSelected = true,
            selectedChart = CHART.BAR,
        )
    )

    fun setPeriod(period: StatisticPeriod) {
        this.period.value = period
    }

    fun changeCategoryChecking(category: Category, isChecked: Boolean) {
        if (isChecked) {
            selectedCategories.value = selectedCategories.value + category
        } else {
            selectedCategories.value = selectedCategories.value - category
        }
    }

    private suspend fun countTotalTime(
        categories: List<Category>,
        period: StatisticPeriod
    ): MutableMap<Category, Long> {
        val map = mutableMapOf<Category, Long>()

        val deferredList = mutableListOf<Deferred<Unit>>()

        for (category in categories) {
            val deferred = CoroutineScope(Dispatchers.Default).async {
                val totalTime = when (period) {
                    StatisticPeriod.DAY -> {
                        trackRepository.getTotalTimeForCategoryToday(category)
                    }

                    StatisticPeriod.WEEK -> {
                        trackRepository.getTotalTimeForCategoryThisWeek(category)
                    }

                    StatisticPeriod.MONTH -> {
                        trackRepository.getTotalTimeForCategoryThisMonth(category)
                    }

                    StatisticPeriod.YEAR -> {
                        trackRepository.getTotalTimeForCategoryThisYear(category)
                    }

                    StatisticPeriod.ALL -> {
                        trackRepository.getTotalTimeForCategoryAllTime(category)
                    }
                }

                if (totalTime != null) {
                    withContext(Dispatchers.Main) {
                        map[category] = totalTime
                    }
                }
            }

            deferredList.add(deferred)
        }

        deferredList.awaitAll()

        return map
    }

    fun switchTheAllCategories(isChecked: Boolean) {
        if (isChecked) {
            selectedCategories.value = emptyList()
        } else {
            selectedCategories.value = statisticScreenState.value.categories
        }
    }

    private suspend fun getTotalTimeForCategoryLast30Days(): List<DayTotalTime> {
        val category = statisticScreenState.value.categories.firstOrNull()
        return trackRepository.getTotalTimeForCategoryLast30Days(category ?: return emptyList())
    }
}