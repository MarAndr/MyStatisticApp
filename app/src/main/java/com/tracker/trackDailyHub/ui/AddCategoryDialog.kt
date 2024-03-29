package com.tracker.trackDailyHub.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.tracker.trackDailyHub.database.AppDatabase
import kotlinx.coroutines.launch

@Composable
fun AddCategoryDialog(
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    onTextFieldChange: (String) -> Unit,
    db: AppDatabase,
) {

    var isError by remember {
        mutableStateOf(false)
    }

    var categoryName by remember {
        mutableStateOf("")
    }

    var errorText by remember {
        mutableStateOf("")
    }

    var isNewCategoryUnique by remember {
        mutableStateOf(true)
    }

    val coroutineScope = rememberCoroutineScope()

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
            androidx.compose.material3.OutlinedTextField(
                value = categoryName,
                onValueChange = {
                    isError = it.isBlank()
                    categoryName = it

                    coroutineScope.launch {
                    isNewCategoryUnique = db.categoryDao().getCategoryByName(categoryName) == null
                    }
                    onTextFieldChange(it)
                },
                label = { Text(text = "Название категории") },
                isError = isError,
                singleLine = true,
                trailingIcon = {
                    if (isError)
                        Icon(Icons.Filled.Warning, "error", tint = MaterialTheme.colors.error)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                supportingText = {
                    if (isError) {
                        Text(
                            text = errorText,
                            color = MaterialTheme.colors.error,
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
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
                    onClick = {
                        if (categoryName.isNotBlank()) {
                            if (!isNewCategoryUnique) {
                                isError = true
                                errorText = "Категория с таким названием уже существует"
                            } else {
                                onConfirmClick()
                            }

                        } else {
                            isError = true
                            errorText = "Введите название"
                        }
                    },
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

