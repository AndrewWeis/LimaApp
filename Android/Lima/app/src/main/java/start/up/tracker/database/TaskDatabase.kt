package start.up.tracker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import start.up.tracker.database.dao.*
import start.up.tracker.entities.Article
import start.up.tracker.entities.Category
import start.up.tracker.entities.DayStat
import start.up.tracker.entities.Task
import start.up.tracker.di.ApplicationScope
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [Task::class, Category::class, DayStat::class, Article::class],
    version = 1
)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun articlesDao(): ArticlesDao
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
            val articlesDao = database.get().articlesDao()

            applicationScope.launch {

                // DON'T DELETE. IT'S DEFAULT CATEGORY
                categoriesDao.insertCategory(Category(name = "Inbox"))

                val articles = ArticleStorage.getArticles().toTypedArray()
                articlesDao.insertAllArticles(*articles)
            }
        }
    }
}
