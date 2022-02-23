package start.up.tracker.data.database

import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import start.up.tracker.data.database.dao.ArticlesDao
import start.up.tracker.data.database.dao.TaskDao
import start.up.tracker.data.entities.Article
import start.up.tracker.data.entities.Category
import start.up.tracker.data.entities.DayStat
import start.up.tracker.data.entities.Task
import start.up.tracker.data.relations.TaskCategoryCrossRef
import start.up.tracker.di.ApplicationScope
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [Task::class, Category::class, TaskCategoryCrossRef::class, DayStat::class, Article::class],
    version = 1
)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun articlesDao(): ArticlesDao

    class Callback @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val taskDao = database.get().taskDao()
            val articlesDao = database.get().articlesDao()

            applicationScope.launch {

                // DON'T DELETE. IT'S DEFAULT CATEGORY
                taskDao.insertCategory(Category("Inbox"))

                val articles = ArticleStorage.getArticles().toTypedArray()
                articlesDao.insertAllArticles(*articles)
            }
        }
    }
}
