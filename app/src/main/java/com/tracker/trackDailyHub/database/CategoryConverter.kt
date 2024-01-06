package com.tracker.trackDailyHub.database

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class CategoryConverter {

    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val categoryAdapter: JsonAdapter<Category> = moshi.adapter(Category::class.java)

    @TypeConverter
    fun fromCategory(category: Category): String {
        return categoryAdapter.toJson(category)
    }

    @TypeConverter
    fun toCategory(categoryString: String): Category {
        return categoryAdapter.fromJson(categoryString) ?: throw IllegalArgumentException("Error converting string to Category")
    }
}
