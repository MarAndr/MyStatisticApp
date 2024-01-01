package com.tracker.trackDailyHub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tracker.trackDailyHub.database.Category
import com.tracker.trackDailyHub.database.TimerData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrackDailyViewModel @Inject constructor(
    private val trackRepository: ITrackRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrackUiState(categories = emptyList(), tracks = emptyList()))
    val uiState: StateFlow<TrackUiState> = _uiState.asStateFlow()

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
}