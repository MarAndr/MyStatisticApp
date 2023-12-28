package com.example.mystatisticapp.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class MainScreenNav(
    val route: String,
    val title: String,
    val icon: ImageVector,
){
    object Home: MainScreenNav(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )

    object AddSurvey: MainScreenNav(
        route = "addSurvey",
        title = "AddSurvey",
        icon = Icons.Default.Add
    )
}
