package com.tracker.trackDailyHub

sealed class TimerState{
    object INITIAL: TimerState()
    object RUNNING: TimerState()
    object PAUSED: TimerState()
}
