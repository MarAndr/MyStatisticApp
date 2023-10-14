package com.example.mystatisticapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MyDialog(
    duration: Long,
    items: List<String>,
    onCategorySelected: (String) -> Unit,
    onConfirmRequest: () -> Unit,
    onDismissRequest: () -> Unit
) {

            AlertDialog(
                onDismissRequest = {
                    onDismissRequest()
                },
                title = {
                    Text(text = "Выберите категорию")
                },
                text = {
                    // Display the list in the text part of the dialog
                    Column {
                        items.forEach { item ->
                            Text(
                                text = item,
                                modifier = Modifier.clickable {
                                    onCategorySelected(item)
                                    onConfirmRequest()
                                } // Attach click listener
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            // Обработка нажатия на кнопку "ОК"
                            onConfirmRequest()
                        }
                    ) {
                        Text(text = "ОК")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            // Обработка нажатия на кнопку "Отмена"
                            onDismissRequest()
                        }
                    ) {
                        Text(text = "Отмена")
                    }
                }
            )
        }
