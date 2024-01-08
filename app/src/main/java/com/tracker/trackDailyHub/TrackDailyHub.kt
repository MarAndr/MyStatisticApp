package com.tracker.trackDailyHub


import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tracker.trackDailyHub.database.AppDatabase
import com.tracker.trackDailyHub.database.Category
import com.tracker.trackDailyHub.ui.AddMeasurementScreen
import com.tracker.trackDailyHub.ui.AddNewCategoryScreen
import com.tracker.trackDailyHub.ui.StatisticScreen
import com.tracker.trackdailyhub.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TrackDailyHub(
    navController: NavHostController = rememberNavController(),
    db: AppDatabase,
) {

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    val startScreenViewModel: StartScreenViewModel = viewModel()
    val addMeasurementScreenViewModel: AddMeasurementScreenViewModel = viewModel()
    val addNewCategoryViewModel: AddNewCategoryViewModel = viewModel()

    val coroutineScope = rememberCoroutineScope()
    val createdCategory by remember {
        mutableStateOf(
            Category(
                name = "",
                iconResourceId = R.drawable.solar_play_bold
            )
        )
    }
    var timerState by remember { mutableStateOf<TimerState>(TimerState.INITIAL) }
    var currentTime by remember { mutableLongStateOf(0L) }
    var showBottomBar by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
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
                showBottomBar = true
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
                showBottomBar = false
                AddMeasurementScreen(
                    navController = navController,
                    viewModel = addMeasurementScreenViewModel,
                    snackbarHostState = snackBarHostState,
                    onSaveClick = { startScreenViewModel.dropTimer() },
                    onNavigateBack = {
                        timerState = TimerState.RUNNING
                        coroutineScope.launch {
                            snackBarHostState.showSnackbar("You did not specify a category for the measurement; the current measurement has not been saved.")
                        }
                        coroutineScope.launch {
                            while (timerState == TimerState.RUNNING) {
                                delay(1000)
                                if (timerState == TimerState.RUNNING) {
                                    currentTime++
                                }
                            }
                        }

                        navController.navigateUp()
                    }
                )
            }
            composable(route = TrackDailyHubDestination.AddNewCategoryScreen.route) {
                AddNewCategoryScreen(
                    navHostController = navController,
                    viewModel = addNewCategoryViewModel,
                )
            }
            composable(route = TrackDailyHubDestination.StatisticScreen.route) {
                StatisticScreen(db = db)
            }
        }


//        if (showDialog) {
//            AddCategoryDialog(
//                onConfirmClick = {
//                    val newTimer =
//                        TimerData(category = createdCategory, timeInSeconds = currentTime)
//                    coroutineScope.launch {
//                        snackBarHostState.showSnackbar("30 мин было добавлено в $createdCategory")
//
//                        db.categoryDao()
//                            .insertUniqueCategory(
//                                Category(
//                                    name = createdCategory.name,
//                                    iconResourceId = R.drawable.solar_play_bold
//                                )
//                            )
//                        db.timerDao().insertTimer(newTimer)
//                    }
//                    showDialog = false
//                },
//                onCancelClick = { showDialog = false },
//                onTextFieldChange = {
////                    createdCategory = it
//                },
//                db = db
//            )
//        }
    }
}

//fun formatTime(currentTime: Long): String {
//    val hours = currentTime / 3600
//    val minutes = (currentTime % 3600) / 60
//    val seconds = currentTime % 60
//
//    return "%02d:%02d:%02d".format(hours, minutes, seconds)
//}

sealed class BottomNavItem(val route: String, val resourceId: Int, val icon: ImageVector) {
    object StartScreen : BottomNavItem(
        TrackDailyHubDestination.StartScreen.route,
        R.string.start_screen,
        Icons.Default.Home
    )

    object StatisticScreen : BottomNavItem(
        TrackDailyHubDestination.StatisticScreen.route,
        R.string.statistic_screen,
        Icons.Default.ThumbUp
    )
}

val items = listOf(
    BottomNavItem.StartScreen,
    BottomNavItem.StatisticScreen
)