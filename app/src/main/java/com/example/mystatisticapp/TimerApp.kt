
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystatisticapp.AppDatabase
import com.example.mystatisticapp.Category
import com.example.mystatisticapp.MyDialog
import com.example.mystatisticapp.TimerData
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
        val categories = listOf("Work", "Cooking", "Exercise", "Study", "Other")

        Text(
            text = if (isRunning) "$currentTime" else "",
            style = MaterialTheme.typography.h6,
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
            MyDialog(
                duration = currentTime,
                items = categories,
                onConfirmRequest = {
                    showDialog = false

                    if (!createdCategory.isNullOrBlank()){
                        coroutineScope.launch {
                            db.categoryDao().insertCategory(Category(name = createdCategory))
                        }

                        Log.d("MY_TAG", "HomeScreen: createdCategory = $createdCategory")
                    } else if (selectedCategory.value.isNotEmpty()) {
                        // Если выбрана категория, сохраните данные в базу данных
                        val newTimer = TimerData(category = selectedCategory.value, timeInSeconds = currentTime)
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


//@Composable
//fun TimerItem(timer: TimerData) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//        elevation = 4.dp
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            Text(
//                text = "Category: ${timer.category}",
//                style = MaterialTheme.typography.h1
//            )
//            Text(
//                text = "Time: ${formatTime(timer.timeInSeconds.toInt())}", // Format time as needed
//                style = MaterialTheme.typography.body1
//            )
//        }
//    }
//}


// Helper function to format time in seconds to a readable format (e.g., "25 minutes")
@Composable
fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return if (minutes > 0) {
        "$minutes minutes"
    } else {
        "$remainingSeconds seconds"
    }
}

@Composable
@Preview
fun TimerAppPreview() {
//    val fakeTimers = listOf(
//        TimerData(category = "Work", timeInSeconds = 1500), // Example timer data
//        TimerData(category = "Cooking", timeInSeconds = 900) // Example timer data
//    )
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "", // Initially, the timer is stopped
//            style = MaterialTheme.typography.h1,
//            modifier = Modifier.padding(16.dp)
//        )
//
//        Button(onClick = {
//            // Simulate starting the timer
//        }) {
//            Text(text = "Start")
//        }
//
//        // Simulate a list of timers from the database
//        for (timer in fakeTimers) {
//            TimerItem(timer = timer)
//        }
//    }
}
