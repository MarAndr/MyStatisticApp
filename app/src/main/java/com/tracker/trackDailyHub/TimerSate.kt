package com.tracker.trackDailyHub

sealed class TimerSate{
    object INITIAL: TimerSate()
    object RUNNING: TimerSate()
    object STOPPED: TimerSate()
    object PAUSED: TimerSate()
}
