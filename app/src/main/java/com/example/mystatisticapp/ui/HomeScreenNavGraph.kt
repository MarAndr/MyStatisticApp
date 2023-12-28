package com.example.mystatisticapp.ui

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mystatisticapp.database.AppDatabase

@Composable
fun HomeScreenNavGraph(
    navHostController: NavHostController,
    db: AppDatabase,
    duration: Long,
    items: List<String>,
    onCategoryCreated: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onConfirmRequest: () -> Unit,
    onDismissRequest: () -> Unit
){
    NavHost(navController = navHostController, startDestination = BottomBarScreen.Home.route){
        composable(
            route = MainScreenNav.Home.route
        ){
            HomeScreen(db)
        }

        composable(
            route = MainScreenNav.AddSurvey.route
        ){
            AddSurveyScreen(duration, items, onCategoryCreated, onCategorySelected, onConfirmRequest, onDismissRequest)
        }
    }
}