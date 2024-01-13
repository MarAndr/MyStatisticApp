package com.tracker.trackDailyHub

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tracker.trackDailyHub.database.Category
import com.tracker.trackDailyHub.database.TimerData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AddNewCategoryViewModel @Inject constructor(
    private val repository: ITrackRepository,
) : ViewModel() {

    private val _newCategoryData = MutableStateFlow(NewCategoryData())
    val newCategoryData: StateFlow<NewCategoryData> = _newCategoryData

    private val _errorState = MutableStateFlow(AddNewCategoryValidationState())
    val errorState: StateFlow<AddNewCategoryValidationState> = _errorState

    private val _events = MutableSharedFlow<AddNewCategoryEvent>()
    val events = _events.asSharedFlow()

    fun setCategoryName(name: String) {
        _newCategoryData.value = _newCategoryData.value.copy(categoryName = name)
    }

    fun setCategoryColor(color: Color) {
        _newCategoryData.value = _newCategoryData.value.copy(color = color)
    }

    fun setCategoryIcon(iconId: Int) {
        _newCategoryData.value = _newCategoryData.value.copy(icon = iconId)
    }

    fun addTrackWithNewCategory(time: Long) = viewModelScope.launch {
        val validationResult = validateNewCategoryData(_newCategoryData.value)
        _errorState.value = validationResult
        handleEvents(validationResult)
        if (!validationResult.isValid()) {
            return@launch
        }

        val category = Category(
            name = _newCategoryData.value.categoryName,
            iconResourceId = _newCategoryData.value.icon ?: 0,
            color = _newCategoryData.value.color?.toArgb() ?: 0,
        )
        repository.insertCategory(category)
        val addedCategory = repository.getCategories().find { it.name == _newCategoryData.value.categoryName }

        val track = TimerData(
            category = addedCategory ?: return@launch,
            timeInSeconds = time,
        )
        repository.insertTrack(track)

        _events.emit(AddNewCategoryEvent.ValidationSuccess)
    }

    private fun AddNewCategoryValidationState.isValid(): Boolean {
        return !isCategoryFieldEmpty &&
                !isCategoryNameNotUnique &&
                !isColorNotChosen &&
                !isIconNotChosen
    }

    private fun validateNewCategoryData(data: NewCategoryData): AddNewCategoryValidationState {
        return runBlocking {
            AddNewCategoryValidationState(
                isCategoryFieldEmpty = data.categoryName.isEmpty(),
                isCategoryNameNotUnique = !repository.isCategoryNameUnique(data.categoryName),
                isColorNotChosen = data.color == null,
                isIconNotChosen = data.icon == null
            )
        }
    }

    private fun handleEvents(validationState: AddNewCategoryValidationState) {
        viewModelScope.launch {
            val event = when {
                validationState.isCategoryFieldEmpty -> AddNewCategoryEvent.CategoryFieldEmpty
                validationState.isCategoryNameNotUnique -> AddNewCategoryEvent.CategoryNameNotUnique
                validationState.isColorNotChosen -> AddNewCategoryEvent.ColorNotChosen
                validationState.isIconNotChosen -> AddNewCategoryEvent.IconNotChosen
                else -> null
            }
            _events.emit(event ?: return@launch)
        }
    }
}
