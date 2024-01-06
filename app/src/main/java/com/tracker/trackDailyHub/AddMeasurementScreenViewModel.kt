package com.tracker.trackDailyHub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tracker.trackDailyHub.database.Category
import com.tracker.trackDailyHub.database.TimerData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMeasurementScreenViewModel @Inject constructor(
    private val trackRepository: ITrackRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            loadCategories()
        }
    }

    private val _addMeasurementScreenState = MutableStateFlow(
        AddMeasurementScreenState(categoriesNames = listOf(), tracks = listOf())
    )
    val addMeasurementScreenState: StateFlow<AddMeasurementScreenState> = _addMeasurementScreenState.asStateFlow()
    private val currentState = _addMeasurementScreenState.value

    fun addTrackWithNewCategory(categoryName: String, track: TimerData) {
        viewModelScope.launch {
            trackRepository.insertCategory(Category(name = categoryName))
            trackRepository.insertTrack(track)
        }
    }

    fun addTrackWithExistedCategory(track: TimerData) {
        viewModelScope.launch {
            trackRepository.insertTrack(track)
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            trackRepository.getCategories()
                .collect { categories ->
                    val categoriesNames = categories.map { it.name }
                    _addMeasurementScreenState.value = currentState.copy(categoriesNames = categoriesNames)
                }
        }
    }
}