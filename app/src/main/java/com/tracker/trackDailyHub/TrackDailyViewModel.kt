package com.tracker.trackDailyHub

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TrackDailyViewModel constructor(
    private val trackRepository: ITrackRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrackUiState(categories = emptyList(), tracks = emptyList()))
    val uiState: StateFlow<TrackUiState> = _uiState.asStateFlow()
//
//    fun addTrackWithNewCategory(categoryName: String, track: Track) {
//        viewModelScope.launch {
//            trackRepository.insertCategory(Category(name = categoryName))
//            trackRepository.insertTrack(track)
//        }
//    }
//
//    fun addTrackWithExistedCategory(track: Track) {
//        viewModelScope.launch {
//            trackRepository.insertTrack(track)
//        }
//    }
}