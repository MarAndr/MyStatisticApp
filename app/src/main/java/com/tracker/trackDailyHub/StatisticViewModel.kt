package com.tracker.trackDailyHub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tracker.trackDailyHub.database.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val trackRepository: ITrackRepository
) : ViewModel() {


    private val _statisticScreenState = MutableStateFlow(
        StatisticScreenState(
            categories = emptyList(),
            selectedCategories = emptyList(),
            selectedPeriod = StatisticPeriod.DAY,
            isAllCategoriesSelected = true,
            selectedChart = CHART.BAR,
        )
    )
    val statisticScreenState: StateFlow<StatisticScreenState> = _statisticScreenState

    fun setPeriod(period: StatisticPeriod) {
        _statisticScreenState.value =
            _statisticScreenState.value.copy(selectedPeriod = period)
    }

    fun changeCategoryChecking(category: Category, isChecked: Boolean) {
        val currentSelectedCategories = _statisticScreenState.value.selectedCategories
        val list = mutableListOf<Category>()
        list.addAll(currentSelectedCategories)
        if (isChecked) {
            list.add(category)
        } else {
            list.remove(category)
        }
        _statisticScreenState.value = _statisticScreenState.value.copy(selectedCategories = list)
    }

    private suspend fun countTotalTime(categories: List<Category>) {
        val map = mutableMapOf<Category, Long>()

        val deferredList = mutableListOf<Deferred<Unit>>()

        for (category in categories) {
            val deferred = CoroutineScope(Dispatchers.Default).async {
                val totalTime = trackRepository.getTotalTimeForCategory(category).firstOrNull()
                if (totalTime != null) {
                    withContext(Dispatchers.Main) {
                        map[category] = totalTime
                    }
                }
            }

            deferredList.add(deferred)
        }

        deferredList.awaitAll()

        _statisticScreenState.value =
            _statisticScreenState.value.copy(totalTimeForEachCategory = map)
    }

    fun switchTheAllCategories(isChecked: Boolean) {
        val isAllCategoriesChecked = _statisticScreenState.value.isAllCategoriesSelected

        if (isChecked) {
            _statisticScreenState.value =
                _statisticScreenState.value.copy(selectedCategories = emptyList())
        } else {
            val selectedCategories = mutableListOf<Category>()
            selectedCategories.addAll(_statisticScreenState.value.categories)
            _statisticScreenState.value =
                _statisticScreenState.value.copy(selectedCategories = selectedCategories)
        }

        _statisticScreenState.value =
            _statisticScreenState.value.copy(isAllCategoriesSelected = !isAllCategoriesChecked)
    }

    fun loadCategories() {
        viewModelScope.launch {
            trackRepository.getCategories()
                .collect { categories ->
                    _statisticScreenState.value =
                        _statisticScreenState.value.copy(categories = categories)
                    countTotalTime(categories)
                }

        }
    }


}