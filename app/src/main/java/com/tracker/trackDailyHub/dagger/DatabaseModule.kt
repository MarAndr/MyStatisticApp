package com.tracker.trackDailyHub.dagger

import android.content.Context
import android.content.res.Resources
import androidx.room.Room
import com.tracker.trackDailyHub.database.AppDatabase
import com.tracker.trackDailyHub.database.Category
import com.tracker.trackDailyHub.database.CategoryDao
import com.tracker.trackDailyHub.database.TimerDao
import com.tracker.trackdailyhub.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "timer-database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @Provides
    @Singleton
    fun provideCategoryDao(appDatabase: AppDatabase, resources: Resources): CategoryDao {
        runBlocking {
            withContext(Dispatchers.IO) {
                val defaultCategories = resources.getStringArray(R.array.default_categories)
                val defaultIcons = resources.obtainTypedArray(R.array.default_category_icons)

                val categories = ArrayList<Category>()

                for (i in defaultCategories.indices) {
                    val category = Category(
                        name = defaultCategories[i],
                        iconResourceId = defaultIcons.getResourceId(i, 0)
                    )
                    categories.add(category)
                }

                defaultIcons.recycle()
                appDatabase.categoryDao().insertCategories(categories = categories)
            }
        }
        return appDatabase.categoryDao()
    }

    @Provides
    @Singleton
    fun provideTrackDao(appDatabase: AppDatabase): TimerDao {
        return appDatabase.timerDao()
    }
}