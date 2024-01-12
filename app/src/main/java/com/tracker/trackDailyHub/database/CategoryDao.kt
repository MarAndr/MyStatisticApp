package com.tracker.trackDailyHub.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM Category")
    suspend fun getAllCategories(): List<Category>

    @Query("SELECT * FROM Category")
    fun getAllCategoriesFlow(): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<Category>)

    @Query("SELECT * FROM Category WHERE name = :categoryName LIMIT 1")
    suspend fun getCategoryByName(categoryName: String): Category?

    @Transaction
    suspend fun insertUniqueCategory(category: Category) {
        val existingCategory = getCategoryByName(category.name)
        if (existingCategory == null) {
            insertCategory(category)
        } else {
            throw IllegalArgumentException("Category with the same name already exists")
        }
    }

    @Query("SELECT COUNT(*) FROM Category WHERE name = :categoryName")
    suspend fun getCategoryCountByName(categoryName: String): Int

    @Transaction
    suspend fun isCategoryNameUnique(categoryName: String): Boolean {
        val count = getCategoryCountByName(categoryName)
        return count == 0
    }
}