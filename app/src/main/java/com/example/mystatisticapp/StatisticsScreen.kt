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
    val categoryToTotalTime = mutableMapOf<String, Long>()

    timersState.forEach { timerData ->
        categoryToTotalTime[timerData.category] =
            categoryToTotalTime.getOrDefault(timerData.category, 0) + timerData.timeInSeconds
    }

    SectionList(sections = categoryToTotalTime)
}


@Composable
fun SectionList(sections: Map<String, Long>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(sections.toList()) { (category, timeInSeconds) ->
            SectionItem(category, timeInSeconds)
        }
    }
}

@Composable
fun SectionItem(category: String, duration: Long) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = "Название раздела: $category")
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Продолжительность: $duration")
    }
}