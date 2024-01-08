package com.tracker.trackDailyHub.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tracker.trackDailyHub.AddNewCategoryValidationState
import com.tracker.trackDailyHub.AddNewCategoryViewModel
import com.tracker.trackDailyHub.ui.theme.Green800
import com.tracker.trackdailyhub.R
import kotlinx.coroutines.launch

@Composable
fun AddNewCategoryScreen(
    viewModel: AddNewCategoryViewModel,
    navHostController: NavHostController,
) {

    var createdCategoryName by remember {
        mutableStateOf("")
    }

    var chosenCategoryIcon by remember {
        mutableIntStateOf(0)
    }

    var chosenCategoryColor by remember {
        mutableStateOf(Color.White)
    }

    var isError by remember {
        mutableStateOf(false)
    }

    var supportText by remember {
        mutableStateOf("")
    }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(viewModel.errorState) {
        viewModel.errorState.collect { errorState ->
            renderValidationResults(errorState) {
                when (it) {
                    CategoryError.EMPTY_FIELD -> {
                        isError = true
                        supportText = it.supportText
                    }

                    CategoryError.NOT_UNIQUE_CATEGORY -> {
                        isError = true
                        supportText = it.supportText
                    }

                    CategoryError.COLOR_NOT_CHOSEN -> {
                        isError = true
                        supportText = it.supportText
                    }

                    CategoryError.ICON_NOT_CHOSEN -> {
                        isError = true
                        supportText = it.supportText
                    }
                }
            }
        }
    }

    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            label = {
                Text(text = "Type New Category")
            },
            isError = isError,
            supportingText = {
                Text(text = supportText, color = MaterialTheme.colors.error)
            },
            value = createdCategoryName,
            onValueChange = {
                createdCategoryName = it
                viewModel.setCategoryName(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
        Row(modifier = Modifier.padding(20.dp)) {
            Icon(
                modifier = Modifier.padding(end = 12.dp),
                painter = painterResource(id = R.drawable.ic_choose_icon),
                tint= Color.Unspecified,
                contentDescription = ""
            )
            Icon(
                painter = painterResource(id = R.drawable.choose_color),
                tint= Color.Unspecified,
                contentDescription = ""
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Green800,
                    contentColor = Color.White,
                ),
                shape = RoundedCornerShape(16.dp),
                onClick = {
                    coroutineScope.launch {
                        viewModel.createNewCategory()
                    }
                }
            ) {
                Text(text = "Save")
            }
        }
        Spacer(modifier = Modifier.height(50.dp))
    }
}

fun renderValidationResults(
    errorState: AddNewCategoryValidationState,
    onErrorEvent: (CategoryError) -> Unit
) {
    when {
        errorState.isCategoryFieldEmpty -> {
            onErrorEvent(CategoryError.EMPTY_FIELD)
        }

        errorState.isCategoryNameNotUnique -> {
            onErrorEvent(CategoryError.NOT_UNIQUE_CATEGORY)
        }

        errorState.isColorNotChosen -> {
            onErrorEvent(CategoryError.COLOR_NOT_CHOSEN)

        }

        errorState.isIconNotChosen -> {
            onErrorEvent(CategoryError.ICON_NOT_CHOSEN)
        }
    }
}

enum class CategoryError(val supportText: String) {
    EMPTY_FIELD("The field is empty"),
    NOT_UNIQUE_CATEGORY("The category is not unique"),
    COLOR_NOT_CHOSEN("The color is not chosen"),
    ICON_NOT_CHOSEN("The icon is not chosen"),
}
