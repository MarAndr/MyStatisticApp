package com.tracker.trackDailyHub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tracker.trackDailyHub.database.Category
import com.tracker.trackDailyHub.database.TimerData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartScreenViewModel @Inject constructor(
    private val trackRepository: ITrackRepository
) : ViewModel() {

    private val _startScreenState = MutableStateFlow(StartScreenState(time = 0L, timerState = TimerState.INITIAL))
    val startScreenState: StateFlow<StartScreenState> = _startScreenState.asStateFlow()
    private val currentState = _startScreenState.value


    fun startTimer() {
        _startScreenState.value = _startScreenState.value.copy(timerState = TimerState.RUNNING)

        viewModelScope.launch {
            while (_startScreenState.value.timerState == TimerState.RUNNING) {
                delay(1000)
                if (_startScreenState.value.timerState == TimerState.RUNNING) {
                    val currentState = _startScreenState.value
                    _startScreenState.value = currentState.copy(time = currentState.time + 1L)
                }
            }
        }
    }

    fun pauseTimer(){
        _startScreenState.value = _startScreenState.value.copy(timerState = TimerState.PAUSED)
    }

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