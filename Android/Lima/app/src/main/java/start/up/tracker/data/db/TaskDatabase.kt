package start.up.tracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import start.up.tracker.data.models.Category
import start.up.tracker.data.models.Task
import start.up.tracker.data.relations.TaskCategoryCrossRef
import start.up.tracker.di.ApplicationScope
import javax.inject.Inject
import javax.inject.Provider


@Database(entities = [Task::class, Category::class, TaskCategoryCrossRef::class], version = 1)
abstract class TaskDatabase: RoomDatabase() {

    abstract fun taskDao(): TaskDao

    class Callback @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().taskDao()

            applicationScope.launch {
                
                // DON'T DELETE. IT'S DEFAULT CATEGORY
                dao.insertCategory(Category("Inbox"))

                dao.insertCategory(Category("Eng"))
            }
        }
    }
}