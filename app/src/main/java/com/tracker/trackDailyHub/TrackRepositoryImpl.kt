package com.tracker.trackDailyHub

import android.content.res.Resources
import com.tracker.trackDailyHub.database.Category
import com.tracker.trackDailyHub.database.CategoryDao
import com.tracker.trackDailyHub.database.TimerDao
import com.tracker.trackDailyHub.database.TimerData
import com.tracker.trackdailyhub.R
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TrackRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val timerDao: TimerDao,
    private val resources: Resources,
) : ITrackRepository {
    override suspend fun insertCategory(category: Category) {
        categoryDao.insertUniqueCategory(category)
    }

    override suspend fun insertTrack(track: TimerData) {
        timerDao.insertTimer(track)
    }

    override suspend fun getTotalTimeForCategory(category: Category): Flow<Long?> {
        return timerDao.getTotalTimeForCategory(category)
    }

    override suspend fun isCategoryNameUnique(categoryName: String): Boolean {
        return categoryDao.isCategoryNameUnique(categoryName)
    }

    override suspend fun getCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories()
    }

    override suspend fun insertDefaultCategories() {
        val defaultCategories = resources.getStringArray(R.array.default_categories)
        val defaultIcons = resources.obtainTypedArray(R.array.default_category_icons)
        val categories = ArrayList<Category>()
        for (index in defaultCategories.indices) {
            val category = Category(
                name = defaultCategories[index],
                iconResourceId = defaultIcons.getResourceId(index, 0)
            )
            categories.add(category)
        }
        defaultIcons.recycle()
        return categoryDao.insertCategories(categories)
    }
}