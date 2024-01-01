package com.tracker.trackDailyHub.ui

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tracker.trackDailyHub.database.AppDatabase

@Composable
fun BottomNavGraph(navHostController: NavHostController, db: AppDatabase){
    NavHost(navController = navHostController, startDestination = BottomBarScreen.Home.route){
        composable(
            route = BottomBarScreen.Home.route
        ){
            HomeScreen(db, navHostController)
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