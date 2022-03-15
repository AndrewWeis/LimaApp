package start.up.tracker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import start.up.tracker.database.dao.*
import start.up.tracker.entities.Technique
import start.up.tracker.entities.Category
import start.up.tracker.entities.DayStat
import start.up.tracker.entities.Task
import start.up.tracker.di.ApplicationScope
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [Task::class, Category::class, DayStat::class, Technique::class],
    version = 1
)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun techniqueDao(): TechniquesDao
    abstract fun analyticsDao(): AnalyticsDao
    abstract fun categoriesDao(): CategoriesDao
    abstract fun todayTasksDao(): TodayTasksDao
    abstract fun calendarTasksDao(): CalendarTasksDao
    abstract fun upcomingTasksDao(): UpcomingTasksDao

    class Callback @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val categoriesDao = database.get().categoriesDao()
            val techniqueDao = database.get().techniqueDao()

            applicationScope.launch {

                // DON'T DELETE. IT'S DEFAULT CATEGORY
                categoriesDao.insertCategory(Category(name = "Inbox"))

                val techniques = TechniquesStorage.getTechniques().toTypedArray()
                techniqueDao.insertAllTechniques(*techniques)
            }
        }
    }
}
