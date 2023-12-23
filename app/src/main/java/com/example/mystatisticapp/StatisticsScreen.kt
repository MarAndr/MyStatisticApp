package com.example.mystatisticapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun StatisticScreen(db: AppDatabase) {
    val timers = db.timerDao().getAllTimers()
    val timersState = timers.collectAsState(initial = listOf()).value
    val finalList = mutableListOf<TimerData>()
    var currentCategory = ""
    var totalTimeInSeconds = 0L

    timersState.forEach { timerData ->
        if (timerData.category == currentCategory) {
            // Если категория совпадает, добавьте время к общей продолжительности
            totalTimeInSeconds += timerData.timeInSeconds
        } else {
            // Если категория отличается, добавьте предыдущую категорию и продолжительность
            if (currentCategory.isNotEmpty()) {
                finalList.add(TimerData(category = currentCategory, timeInSeconds = totalTimeInSeconds))
            }
            currentCategory = timerData.category
            totalTimeInSeconds = timerData.timeInSeconds
        }
    }

    // Добавьте последнюю категорию и продолжительность
    if (currentCategory.isNotEmpty()) {
        finalList.add(TimerData(category = currentCategory, timeInSeconds = totalTimeInSeconds))
    }

    SectionList(sections = finalList)
}


@Composable
fun SectionList(sections: List<TimerData>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(sections) { section ->
            SectionItem(section)
        }
    }
}

@Composable
fun SectionItem(section: TimerData) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = "Название раздела: ${section.category}")
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Продолжительность: ${section.timeInSeconds}")
    }
}