package com.tracker.trackDailyHub

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

data class NewCategoryData(
    val categoryName: String = "",
    @DrawableRes val icon: Int? = null,
    val color: Color? = null,
)
