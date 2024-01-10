package com.tracker.trackDailyHub

sealed class AddNewCategoryEvent {
    object CategoryFieldEmpty : AddNewCategoryEvent()
    object CategoryNameNotUnique : AddNewCategoryEvent()
    object ColorNotChosen : AddNewCategoryEvent()
    object IconNotChosen : AddNewCategoryEvent()
    object ValidationSuccess : AddNewCategoryEvent()
}
