package com.tracker.trackDailyHub.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.tracker.trackDailyHub.AddNewCategoryValidationState
import com.tracker.trackDailyHub.AddNewCategoryViewModel
import com.tracker.trackDailyHub.TrackDailyHubDestination
import com.tracker.trackDailyHub.categoriesIcons
import com.tracker.trackDailyHub.popularColors1
import com.tracker.trackDailyHub.popularColors2
import com.tracker.trackDailyHub.popularColors3
import com.tracker.trackDailyHub.popularColors4
import com.tracker.trackDailyHub.popularColors5
import com.tracker.trackDailyHub.popularColors6
import com.tracker.trackDailyHub.ui.theme.Green800
import com.tracker.trackdailyhub.R
import kotlinx.coroutines.launch

@Composable
fun AddNewCategoryScreen(
    viewModel: AddNewCategoryViewModel,
    navController: NavHostController,
) {

    var createdCategoryName by remember {
        mutableStateOf("")
    }

    var chosenCategoryIcon by remember {
        mutableIntStateOf(R.drawable.ic_choose_icon)
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

    var isColorClicked by remember {
        mutableStateOf(false)
    }

    var isIconClicked by remember {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val timeArg = navBackStackEntry?.arguments?.getLong("time") ?: 0L

    LaunchedEffect(viewModel.newCategoryData) {
        viewModel.newCategoryData.collect {
            chosenCategoryColor = it.color ?: Color.Unspecified
            chosenCategoryIcon = it.icon ?: R.drawable.ic_choose_icon
        }
    }

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
        Text(text = "Save the measurement to a new category", style = MaterialTheme.typography.h2, modifier = Modifier.padding(bottom = 16.dp))
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
                modifier = Modifier
                    .padding(end = 12.dp)
                    .border(1.dp, color = Color.LightGray)
                    .clickable {
                        isIconClicked = !isIconClicked
                    },
                painter = painterResource(id = chosenCategoryIcon),
                tint = Color.Unspecified,
                contentDescription = ""
            )
            Icon(
                modifier = Modifier
                    .border(1.dp, color = Color.LightGray)
                    .clickable {
                        isColorClicked = !isColorClicked
                    },
                painter = painterResource(id = R.drawable.choose_color),
                tint = chosenCategoryColor,
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
                        viewModel.addTrackWithNewCategory(timeArg)
                    }
                    navController.navigate(route = TrackDailyHubDestination.StartScreen.route).apply {

                    }
                }
            ) {
                Text(text = "Save")
            }
        }
        if (isColorClicked) {
            ColorGrid(viewModel)
        }

        if (isIconClicked){
            IconGrid(viewModel)
        }
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

@Composable
fun ColorGrid(viewModel: AddNewCategoryViewModel) {
    val colors = getColors()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        itemsIndexed(colors) { rowIndex, colorRow ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                colorRow.forEach { color ->
                    ColoredSquare(color = color, viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun IconGrid(viewModel: AddNewCategoryViewModel) {
    val icons = getIcons()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        itemsIndexed(icons) { rowIndex, colorRow ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                colorRow.forEach { icon ->
                    IconsSquare(icon = icon, viewModel = viewModel)
                }
            }
        }
    }
}

fun getColors(): List<List<Color>> {
    val colors = mutableListOf<List<Color>>()
    colors.addAll(
        listOf(
            popularColors1,
            popularColors2,
            popularColors3,
            popularColors4,
            popularColors5,
            popularColors6
        )
    )
    return colors
}

fun getIcons(): List<List<Int>> {
    val icons = mutableListOf<List<Int>>()
    icons.add(categoriesIcons)
    return icons
}

@Composable
fun ColoredSquare(color: Color, viewModel: AddNewCategoryViewModel) {
    Box(
        modifier = Modifier
            .clickable {
                viewModel.setCategoryColor(color)
            }
            .border(1.dp, color = Color.LightGray)
            .size(36.dp)
            .padding(4.dp)
            .background(color),
    ) {
        // Content of the square, if any
    }
}

@Composable
fun IconsSquare(icon: Int, viewModel: AddNewCategoryViewModel) {
    Box(
        modifier = Modifier
            .clickable {
                viewModel.setCategoryIcon(icon)
            }
            .border(1.dp, color = Color.LightGray)
            .size(36.dp)
            .padding(4.dp),
    ) {
        Image(painter = painterResource(id = icon), contentDescription = "")
    }
}
