import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystatisticapp.database.AppDatabase
import com.example.mystatisticapp.database.Category
import com.example.mystatisticapp.database.TimerData
import com.example.mystatisticapp.ui.AddSurveyScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(db: AppDatabase) {

    val coroutineScope = rememberCoroutineScope()
    val selectedCategory = remember { mutableStateOf("") }
    var createdCategory by remember { mutableStateOf("") }
    var isRunning by remember { mutableStateOf(false) }
    var currentTime by remember { mutableLongStateOf(0L) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Create a list of categories
//        val categories = listOf("Work", "Cooking", "Exercise", "Study", "Other")
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

        Text(
            fontFamily = FontFamily.Monospace,
            text = formattedTime,
            style = MaterialTheme.typography.h2,
            modifier = Modifier.padding(16.dp)
        )

        if (!isRunning) {
            Button(onClick = {
                isRunning = true
                currentTime = 0L
                coroutineScope.launch {
                    while (isRunning) {
                        delay(1000)
                        currentTime++
                    }
                }
            }) {
                Text(fontSize = 18.sp, text = "Start")
            }
        } else {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = {
                    isRunning = false
                    showDialog = true // Show the dialog
                }) {
                    Text(text = "Stop")
                }

                Button(onClick = {
                    isRunning = false
                }) {
                    Text(text = "Pause")
                }
            }
        }

        if (showDialog) {
            AddSurveyScreen(
                duration = currentTime,
                items = categoriesNames,
                onConfirmRequest = {
                    showDialog = false

                    if (!createdCategory.isNullOrBlank()) {
                        val newTimer =
                            TimerData(category = createdCategory, timeInSeconds = currentTime)
                        coroutineScope.launch {
                            db.categoryDao().insertCategory(Category(name = createdCategory))
                            db.timerDao().insertTimer(newTimer)
                        }

                    } else if (selectedCategory.value.isNotEmpty()) {
                        // Если выбрана категория, сохраните данные в базу данных
                        val newTimer = TimerData(
                            category = selectedCategory.value,
                            timeInSeconds = currentTime
                        )
                        coroutineScope.launch {
                            db.timerDao().insertTimer(newTimer)
                        }
                    }
                },
                onDismissRequest = {
                    showDialog = false
                }, onCategorySelected = {
                    selectedCategory.value = it
                },
                onCategoryCreated = {
                    createdCategory = it
                }
            )
        }
    }
}

