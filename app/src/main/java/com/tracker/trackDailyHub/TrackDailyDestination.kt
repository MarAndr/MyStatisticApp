package com.tracker.trackDailyHub

import com.tracker.trackDailyHub.database.Category

sealed class TrackDailyHubDestination(val route: String){
    object StartScreen: TrackDailyHubDestination("StartScreen")
    object AddSurveyScreen: TrackDailyHubDestination("AddSurveyScreen")
    object StatisticScreen: TrackDailyHubDestination("StatisticScreen")
    object AddNewCategoryScreen: TrackDailyHubDestination("AddNewCategoryScreen")

    fun withArgs(vararg args: Long): String{
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    fun withArgs(vararg args: Category): String{
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}