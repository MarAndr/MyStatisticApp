package com.example.mystatisticapp.navigation

sealed class HomeScreenDestination(val route: String) {
    object StartHomeScreen : HomeScreenDestination("HomeScreen")
    object AddSurveyScreen : HomeScreenDestination("AddSurveyScreen")
}