package com.tracker.trackDailyHub

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import com.tracker.trackDailyHub.database.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AddNewCategoryViewModel @Inject constructor(
    private val repository: ITrackRepository,
) : ViewModel() {

    private val _newCategoryData = MutableStateFlow(NewCategoryData())
//    val newCategoryData: StateFlow<NewCategoryData> = _newCategoryData

    private val _errorState = MutableStateFlow(AddNewCategoryValidationState())
    val errorState: StateFlow<AddNewCategoryValidationState> = _errorState

    private val _events = MutableStateFlow<AddNewCategoryEvent?>(null)
    val events: StateFlow<AddNewCategoryEvent?> = _events

    fun setCategoryName(name: String){
        _newCategoryData.value = _newCategoryData.value.copy(categoryName = name)
    }

    fun setCategoryColor(color: Color){
        _newCategoryData.value = _newCategoryData.value.copy(color = color)
    }

    fun setCategoryIcon(iconId: Int){
        _newCategoryData.value = _newCategoryData.value.copy(icon = iconId)
    }

    suspend fun createNewCategory() {
        val validationResult = validateNewCategoryData(_newCategoryData.value)
        _errorState.value = validationResult
        handleEvents(validationResult)
        if (validationResult.isValid()) {
            val newCategory = Category(
                name = _newCategoryData.value.categoryName,
                color = _newCategoryData.value.color?.toArgb() ?: 0,
                iconResourceId = _newCategoryData.value.icon ?: 0,
            )
            repository.insertCategory(newCategory)
        }
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
        _events.value = when {
            validationState.isCategoryFieldEmpty -> AddNewCategoryEvent.CategoryFieldEmpty
            validationState.isCategoryNameNotUnique -> AddNewCategoryEvent.CategoryNameNotUnique
            validationState.isColorNotChosen -> AddNewCategoryEvent.ColorNotChosen
            validationState.isIconNotChosen -> AddNewCategoryEvent.IconNotChosen
            else -> null
        }
    }
}
