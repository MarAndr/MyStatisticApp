package com.tracker.trackDailyHub


import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tracker.trackDailyHub.database.AppDatabase
import com.tracker.trackDailyHub.database.Category
import com.tracker.trackDailyHub.database.TimerData
import com.tracker.trackDailyHub.ui.AddCategoryDialog
import com.tracker.trackDailyHub.ui.AddSurveyScreen
import com.tracker.trackDailyHub.ui.StatisticScreen
import com.tracker.trackdailyhub.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TrackDailyHub(
    navController: NavHostController = rememberNavController(),
//    viewModel: TrackDailyViewModel = viewModel(),
    db: AppDatabase,
) {


    val snackBarHostState = remember {
        SnackbarHostState()
    }

    val coroutineScope = rememberCoroutineScope()
    val selectedCategory = remember { mutableStateOf("") }
    var createdCategory by remember { mutableStateOf("") }
    var isRunning by remember { mutableStateOf(false) }
    var currentTime by remember { mutableLongStateOf(0L) }
    val categoriesFlow = db.categoryDao().getAllCategories()
    val categories = categoriesFlow.collectAsState(initial = listOf()).value
    val categoriesNames: List<String> = categories.map { it.name }
    var showBottomBar by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }

    val formattedTime = if (isRunning) {
        val hours = currentTime / 3600
        val minutes = (currentTime % 3600) / 60
        val seconds = currentTime % 60

        "%02d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        ""
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.padding(16.dp)
            )
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigation {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    items.forEach { screen ->
                        BottomNavigationItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(stringResource(screen.resourceId)) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = TrackDailyHubDestination.StartScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = TrackDailyHubDestination.StartScreen.route) {
                showBottomBar = true
                StartScreen(
                    time = formattedTime,
                    isRunning = isRunning,
                    onStartButtonClick = {
                        isRunning = true
                        currentTime = 0L
                        coroutineScope.launch {
                            while (isRunning) {
                                delay(1000)
                                currentTime++
                            }
                        }
                    },
                    onStopButtonClick = {
                        isRunning = false
                        navController.navigate(TrackDailyHubDestination.AddSurveyScreen.route)
                    },
                    onPauseButtonClick = {
                        isRunning = false
                    }
                )
            }
            composable(route = TrackDailyHubDestination.AddSurveyScreen.route) {
                showBottomBar = false
                AddSurveyScreen(
                    items = categoriesNames,
                    onCategorySelected = {
                        selectedCategory.value = it
                    },
                    onConfirmRequest = {
                        navController.popBackStack()

                        if (selectedCategory.value.isNotEmpty()) {
                            val newTimer = TimerData(
                                category = selectedCategory.value,
                                timeInSeconds = currentTime
                            )
                            coroutineScope.launch {
                                snackBarHostState.showSnackbar("30 мин было добавлено в ${selectedCategory.value}")
                                db.timerDao().insertTimer(newTimer)
                            }
                        }
                    },
                    onAddButtonClick = {
                        showDialog = true
                    }
                )
            }
            composable(route = TrackDailyHubDestination.StatisticScreen.route) {
                StatisticScreen(db = db)
            }
        }

        if (showDialog) {
            AddCategoryDialog(
                categoryName = createdCategory,
                onConfirmClick = {
                    val newTimer =
                        TimerData(category = createdCategory, timeInSeconds = currentTime)
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar("30 мин было добавлено в ${createdCategory}")
                        db.categoryDao()
                            .insertUniqueCategory(Category(name = createdCategory))
                        db.timerDao().insertTimer(newTimer)
                    }
                    showDialog = false
                },
                onCancelClick = { showDialog = false },
                onTextFieldChange = {
                    createdCategory = it
                },
            )
        }
    }
}

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