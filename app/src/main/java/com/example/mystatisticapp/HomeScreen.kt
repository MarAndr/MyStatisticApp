import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mystatisticapp.database.AppDatabase
import com.example.mystatisticapp.database.Category
import com.example.mystatisticapp.database.TimerData
import com.example.mystatisticapp.navigation.HomeScreenDestination
import com.example.mystatisticapp.ui.AddSurveyScreen
import com.example.mystatisticapp.ui.MainScreenNav
import com.example.mystatisticapp.ui.StartHomeScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(db: AppDatabase, navController: NavHostController) {

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

    NavHost(
        navController = navController,
        startDestination = HomeScreenDestination.StartHomeScreen.route
    ) {
        composable(route = HomeScreenDestination.StartHomeScreen.route) {
            StartHomeScreen(
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
                    navController.navigate(MainScreenNav.AddSurvey.route)
                },
                onPauseButtonClick = {
                    isRunning = false
                }
            )
        }
        composable(route = HomeScreenDestination.AddSurveyScreen.route) {
            AddSurveyScreen(
                items = categoriesNames,
                onCategoryCreated = { createdCategory = it },
                onCategorySelected = { selectedCategory.value = it },
                onConfirmRequest = {
                    navController.popBackStack()

                    if (!createdCategory.isNullOrBlank()) {
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
    }

}

