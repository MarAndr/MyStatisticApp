package com.example.mystatisticapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AddSurveyScreen(
    duration: Long,
    items: List<String>,
    onCategoryCreated: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onConfirmRequest: () -> Unit,
    onDismissRequest: () -> Unit
) {

    var createdCategory by remember {
        mutableStateOf("")
    }

    // Display the list in the text part of the dialog
    Column(modifier = Modifier.background(Color.White)) {
        Text(text = "Выберите категорию или создайте новую")
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            modifier = Modifier.padding(bottom = 16.dp),
            value = createdCategory,
            onValueChange = {
                createdCategory = it
                onCategoryCreated(it)
            })
        items.forEach { item ->
            Text(
                text = item,
                modifier = Modifier.clickable {
                    onCategorySelected(item)
                    onConfirmRequest()
                } // Attach click listener
            )
        }

        Button(
            onClick = {
                // Обработка нажатия на кнопку "ОК"
                onConfirmRequest()
            }
        ) {
            Text(text = "ОК")
        }
        Button(
            onClick = {
                // Обработка нажатия на кнопку "Отмена"
                onDismissRequest()
            }
        ) {
            Text(text = "Отмена")
        }
    }
}

