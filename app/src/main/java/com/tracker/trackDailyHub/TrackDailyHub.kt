package com.tracker.trackDailyHub


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tracker.trackDailyHub.ui.AddMeasurementScreen
import com.tracker.trackDailyHub.ui.AddNewCategoryScreen
import com.tracker.trackDailyHub.ui.StatisticScreen

@Composable
fun TrackDailyHub(
    navController: NavHostController = rememberNavController(),
) {

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.padding(16.dp)
            )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = TrackDailyHubDestination.StartScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = TrackDailyHubDestination.StartScreen.route) {
                val startScreenViewModel: StartScreenViewModel = hiltViewModel()
                StartScreen(
                    navController = navController,
                    viewModel = startScreenViewModel,
                )
            }
            composable(
                route = TrackDailyHubDestination.AddSurveyScreen.route + "/{time}",
                arguments = listOf(
                    navArgument("time") {
                        type = NavType.LongType
                        defaultValue = 0L
                        nullable = false
                    }
                )
            ) {
                val addMeasurementScreenViewModel: AddMeasurementScreenViewModel = hiltViewModel()
                AddMeasurementScreen(
                    navController = navController,
                    viewModel = addMeasurementScreenViewModel,
                    snackbarHostState = snackBarHostState,
                    onSaveClick = {
//                        startScreenViewModel.dropTimer()
                    },
                    onNavigateBack = {
                        navController.navigateUp()
                    }
                )
            }
            composable(route = TrackDailyHubDestination.AddNewCategoryScreen.route + "/{time}",
                arguments = listOf(
                    navArgument("time") {
                        type = NavType.LongType
                        defaultValue = 0L
                        nullable = false
                    }
                )
            ) {
                val addNewCategoryViewModel: AddNewCategoryViewModel = hiltViewModel()
                AddNewCategoryScreen(
                    navController = navController,
                    snackbarHostState = snackBarHostState,
                    viewModel = addNewCategoryViewModel,
                    onSaveClick = {
//                        startScreenViewModel.dropTimer()
                    },
                )
            }
            composable(route = TrackDailyHubDestination.StatisticScreen.route) {
                val statisticViewModel: StatisticViewModel = hiltViewModel()
                StatisticScreen(
                    viewModel = statisticViewModel,
                    navController = navController,
                )
            }
        }
    }
}