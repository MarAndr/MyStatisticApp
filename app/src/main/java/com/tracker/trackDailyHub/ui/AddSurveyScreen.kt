package com.tracker.trackDailyHub.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddSurveyScreen(
    items: List<String>,
    onCategorySelected: (String) -> Unit,
    onConfirmRequest: () -> Unit,
    onAddButtonClick: () -> Unit,
) {
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
            if (items.isNotEmpty()) {

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Выберите категорию:",
                    style = MaterialTheme.typography.h1,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable(onClick = onAddButtonClick)
                ) {
                    Row(modifier = Modifier.padding(0.dp)) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(text = "Cоздать новую", style = MaterialTheme.typography.h2)
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }


                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
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
                            }
                        }
                    }
                )

            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(text = "Ваш список категорий пуст", style = TextStyle(color = Color.Gray, fontSize = 20.sp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable(onClick = onAddButtonClick)
                    ) {
                        Row(modifier = Modifier.padding(0.dp)) {
                            Icon(
                                imageVector = Icons.Default.AddCircle,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                            Text(text = "Cоздать новую", style = MaterialTheme.typography.h2)
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }

        }
    }
}


