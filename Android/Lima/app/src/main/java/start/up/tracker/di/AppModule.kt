package start.up.tracker.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import start.up.tracker.database.TaskDatabase
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
        callback: TaskDatabase.Callback
    ) = Room.databaseBuilder(app, TaskDatabase::class.java, "task_database")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    @Provides
    fun provideTaskDao(db: TaskDatabase) = db.taskDao()

    @Provides
    fun provideTechniquesDao(db: TaskDatabase) = db.techniqueDao()

    @Provides
    fun provideAnalyticsDao(db: TaskDatabase) = db.analyticsDao()

    @Provides
    fun provideCategoriesDao(db: TaskDatabase) = db.categoriesDao()

    @Provides
    fun provideTodayTasksDao(db: TaskDatabase) = db.todayTasksDao()

    @Provides
    fun provideCalendarTasksDao(db: TaskDatabase) = db.calendarTasksDao()

    @Provides
    fun provideUpcomingTasksDao(db: TaskDatabase) = db.upcomingTasksDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope
