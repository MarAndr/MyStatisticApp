package com.example.mystatisticapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AddSurveyScreen(
    items: List<String>,
    onCategoryCreated: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onConfirmRequest: () -> Unit,
    onCancelClick: () -> Unit
) {

    var createdCategory by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 24.dp)
        ) {
            Text(
                text = "Выберите категорию или создайте новую:",
                style = MaterialTheme.typography.h1,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                placeholder = { Text(text = "Название категории") },
                modifier = Modifier.padding(bottom = 16.dp),
                value = createdCategory,
                onValueChange = {
                    createdCategory = it
                    onCategoryCreated(it)
                }
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Занимает все доступное пространство
                horizontalAlignment = Alignment.Start,
                content = {
                    items(items) { category ->
                        Column {
                            Text(
                                style = MaterialTheme.typography.h2,
                                text = category,
                                modifier = Modifier
                                    .clickable {
                                        onCategorySelected(category)
                                        onConfirmRequest()
                                    }
                                    .padding(16.dp)
                            )
                            if (category != items.last()) {
                                Divider(color = Color.Gray, thickness = 1.dp)
                            }
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.weight(0.15f))
        }



        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Button(
                modifier = Modifier
                    .width(100.dp)
                    .height(40.dp),
                onClick = { onConfirmRequest() }
            ) {
                Text(text = "ОК")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                modifier = Modifier
                    .width(100.dp)
                    .height(40.dp),
                onClick = { onCancelClick() }
            ) {
                Text(text = "Отмена")
            }
        }
    }
}


