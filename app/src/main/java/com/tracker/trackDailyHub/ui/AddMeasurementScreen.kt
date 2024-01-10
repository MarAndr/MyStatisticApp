package com.tracker.trackDailyHub.ui

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.tracker.trackDailyHub.AddMeasurementScreenViewModel
import com.tracker.trackDailyHub.TrackDailyHubDestination
import com.tracker.trackDailyHub.database.Category
import com.tracker.trackDailyHub.database.TimerData
import com.tracker.trackDailyHub.ui.theme.Gray100
import com.tracker.trackDailyHub.ui.theme.Green800
import com.tracker.trackdailyhub.R
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Composable
fun AddMeasurementScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    viewModel: AddMeasurementScreenViewModel,
    onSaveClick: () -> Unit,
    onNavigateBack: () -> Unit,
) {

    var categories by remember {
        mutableStateOf<List<Category>>(emptyList())
    }

    val coroutineScope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val timeArg = navBackStackEntry?.arguments?.getLong("time") ?: 0L

    var selectedCategory by remember {
        mutableStateOf(Category(name = "", iconResourceId = R.drawable.solar_play_bold))
    }

    LaunchedEffect(viewModel.addMeasurementScreenState) {
        viewModel.addMeasurementScreenState.collect { state ->
            categories = state.categories
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 104.dp)
        ) {

            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.addMeasurementScreen_title),
                        style = MaterialTheme.typography.h2,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                backgroundColor = Color.White,
                elevation = AppBarDefaults.TopAppBarElevation
            )

            Spacer(modifier = Modifier.height(32.dp))

            AddNewCategoryBlock{
                navController.navigate(TrackDailyHubDestination.AddNewCategoryScreen.withArgs(timeArg))
            }
            CategoriesList(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = {
                    selectedCategory = it
                },
            )
        }

        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Green800,
                contentColor = Color.White,
            ),
            shape = RoundedCornerShape(16.dp),
            onClick = {
                onSaveClick()
                coroutineScope.launch {
                    viewModel.addTrackWithExistedCategory(
                        track = TimerData(
                            category = selectedCategory,
                            timeInSeconds = timeArg,
                            date = LocalDateTime.now()
                        )
                    )
                    snackbarHostState.showSnackbar("$timeArg было добавлено в ${selectedCategory.name}")
                }
                navController.popBackStack()
            },
            enabled = selectedCategory.name.isNotEmpty(),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Text(fontSize = 14.sp, text = "Save", modifier = Modifier.padding(horizontal = 2.dp))
        }
    }

    BackButtonHandler {
        onNavigateBack()
    }
}

@Composable
fun CategoriesList(
    categories: List<Category>,
    selectedCategory: Category,
    onCategorySelected: (Category) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        content = {
            items(categories) { category ->
                val isCategorySelected = selectedCategory == category
                val backgroundColor = if (isCategorySelected) Gray100 else Color.White
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(color = backgroundColor)
                        .clickable { onCategorySelected(category) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        painter = painterResource(id = category.iconResourceId),
                        tint= Color.Unspecified,
                        contentDescription = null,
                    )
                    Text(
                        style = MaterialTheme.typography.body1,
                        text = category.name,
                        modifier = Modifier.padding(16.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(id = R.drawable.lets_icons_edit_fill),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }

            }
        }
    )

}

@Composable
fun AddNewCategoryBlock(onAddButtonClick: () -> Unit) {
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

            Text(
                text = stringResource(id = R.string.addMeasurementScreen_add_new_category),
                style = MaterialTheme.typography.h2
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
    }

}


