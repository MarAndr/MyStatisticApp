package com.example.mystatisticapp

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun BottomNavGraph(navHostController: NavHostController, db: AppDatabase){
    NavHost(navController = navHostController, startDestination = BottomBarScreen.Home.route){
        composable(
            route = BottomBarScreen.Home.route
        ){
            HomeScreen(db)
        }

        composable(
            route = BottomBarScreen.Statistic.route
        ){
            StatisticScreen(db)
        }

        composable(
            route = BottomBarScreen.Settings.route
        ){
            SettingsScreen()
        }
    }
}