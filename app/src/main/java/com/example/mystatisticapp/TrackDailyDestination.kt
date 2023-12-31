package com.example.mystatisticapp

sealed class TrackDailyHubDestination(val route: String){
    object StartScreen: TrackDailyHubDestination("StartScreen")
    object AddSurveyScreen: TrackDailyHubDestination("AddSurveyScreen")
    object StatisticScreen: TrackDailyHubDestination("StatisticScreen")
}