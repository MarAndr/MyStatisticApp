package com.tracker.trackDailyHub.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun AddCategoryDialog(
    categoryName: String,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    onTextFieldChange: (String) -> Unit
) {
    Dialog(
        onDismissRequest = onCancelClick
    ) {
        Column(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = "Добавить категорию:",
                style = MaterialTheme.typography.h1
            )
            OutlinedTextField(
                value = categoryName,
                onValueChange = onTextFieldChange,
                label = { Text(text = "Название категории") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onCancelClick,
                ) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.h2
                    )
                }
                TextButton(
                    onClick = onConfirmClick,
                ) {
                    Text(
                        text = "OK",
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.h2
                    )
                }
            }
        }
    }
}

