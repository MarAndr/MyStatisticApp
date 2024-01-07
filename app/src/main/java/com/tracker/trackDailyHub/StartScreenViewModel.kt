package com.tracker.trackDailyHub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartScreenViewModel @Inject constructor() : ViewModel() {

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

    fun dropTimer(){
        _startScreenState.value = _startScreenState.value.copy(time = 0L, timerState = TimerState.INITIAL)
    }

    fun pauseTimer(){
        _startScreenState.value = _startScreenState.value.copy(timerState = TimerState.PAUSED)
    }
}