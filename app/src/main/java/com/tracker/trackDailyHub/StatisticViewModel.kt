package com.tracker.trackDailyHub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val trackRepository: ITrackRepository
) : ViewModel() {

    init {
        loadCategories()
    }

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

    fun setPeriod(period: StatisticPeriod){
        _statisticScreenState.value =
            _statisticScreenState.value.copy(selectedPeriod = period)
    }

    private fun loadCategories() {
        viewModelScope.launch {
            trackRepository.getCategories()
                .collect { categories ->
                    _statisticScreenState.value =
                        _statisticScreenState.value.copy(categories = categories)
                }
        }
    }


}