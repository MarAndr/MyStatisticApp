package com.example.mystatisticapp


import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mystatisticapp.database.AppDatabase
import com.example.mystatisticapp.database.Category
import com.example.mystatisticapp.database.TimerData
import com.example.mystatisticapp.ui.AddSurveyScreen
import com.example.mystatisticapp.ui.StatisticScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TrackDailyHub(
    navController: NavHostController = rememberNavController(),
    db: AppDatabase,
) {

    val coroutineScope = rememberCoroutineScope()
    val selectedCategory = remember { mutableStateOf("") }
    var createdCategory by remember { mutableStateOf("") }
    var isRunning by remember { mutableStateOf(false) }
    var currentTime by remember { mutableLongStateOf(0L) }
    val categoriesFlow = db.categoryDao().getAllCategories()
    val categories = categoriesFlow.collectAsState(initial = listOf()).value
    val categoriesNames: List<String> = categories.map { it.name }

    val formattedTime = if (isRunning) {
        val hours = currentTime / 3600
        val minutes = (currentTime % 3600) / 60
        val seconds = currentTime % 60

        "%02d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        ""
    }

    Scaffold(bottomBar = {
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
    }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = TrackDailyHubDestination.StartScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = TrackDailyHubDestination.StartScreen.route) {
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
                AddSurveyScreen(
                    items = categoriesNames,
                    onCategoryCreated = { createdCategory = it },
                    onCategorySelected = { selectedCategory.value = it },
                    onConfirmRequest = {
                        navController.popBackStack()
                        if (createdCategory.isNotBlank()) {
                            val newTimer =
                                TimerData(category = createdCategory, timeInSeconds = currentTime)
                            coroutineScope.launch {
                                db.categoryDao().insertCategory(Category(name = createdCategory))
                                db.timerDao().insertTimer(newTimer)
                            }
                        } else if (selectedCategory.value.isNotEmpty()) {
                            val newTimer = TimerData(
                                category = selectedCategory.value,
                                timeInSeconds = currentTime
                            )
                            coroutineScope.launch {
                                db.timerDao().insertTimer(newTimer)
                            }
                        }
                    },
                    onCancelClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable(route = TrackDailyHubDestination.StatisticScreen.route) {
                StatisticScreen(db = db)
            }
        }

    }
}

sealed class BottomNavItem(val route: String, val resourceId: Int, val icon: ImageVector) {
    object StartScreen : BottomNavItem(TrackDailyHubDestination.StartScreen.route, R.string.start_screen, Icons.Default.Home)
    object StatisticScreen : BottomNavItem(TrackDailyHubDestination.StatisticScreen.route, R.string.statistic_screen, Icons.Default.ThumbUp)
}

val items = listOf(
    BottomNavItem.StartScreen,
    BottomNavItem.StatisticScreen
)