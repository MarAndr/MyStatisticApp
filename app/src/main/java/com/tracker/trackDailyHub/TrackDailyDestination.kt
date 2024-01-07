package com.tracker.trackDailyHub

sealed class TrackDailyHubDestination(val route: String){
    object StartScreen: TrackDailyHubDestination("StartScreen")
    object AddSurveyScreen: TrackDailyHubDestination("AddSurveyScreen")
    object StatisticScreen: TrackDailyHubDestination("StatisticScreen")

    fun withArgs(vararg args: Long): String{
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}