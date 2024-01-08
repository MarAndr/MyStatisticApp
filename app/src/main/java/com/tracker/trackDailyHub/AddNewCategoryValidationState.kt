package com.tracker.trackDailyHub

data class AddNewCategoryValidationState(
    val isCategoryNameNotUnique: Boolean = false,
    val isCategoryFieldEmpty: Boolean = false,
    val isColorNotChosen: Boolean = false,
    val isIconNotChosen: Boolean = false,
)
