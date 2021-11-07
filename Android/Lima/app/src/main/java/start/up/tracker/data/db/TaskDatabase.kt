package start.up.tracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import start.up.tracker.data.db.models.Category
import start.up.tracker.data.db.relations.TaskCategoryCrossRef
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
                dao.insertTask(Task("Wash the dishes"))
                dao.insertTask(Task("Do the laundry"))
                dao.insertTask(Task("Buy groceries", important = true))
                dao.insertTask(Task("Prepare food", completed = true))
                dao.insertTask(Task("Call mom"))
                dao.insertTask(Task("Visit grandma", completed = true))
                dao.insertTask(Task("Repair my bike"))
                dao.insertTask(Task("Call Elon Musk"))

                dao.insertCategory(Category("Today"))
                dao.insertCategory(Category("SomeDay"))

                dao.insertTaskCategoryCrossRef(TaskCategoryCrossRef("Wash the dishes", "Today"))
                dao.insertTaskCategoryCrossRef(TaskCategoryCrossRef("Do the laundry", "Today"))
                dao.insertTaskCategoryCrossRef(TaskCategoryCrossRef("Buy groceries", "Today"))
                dao.insertTaskCategoryCrossRef(TaskCategoryCrossRef("Prepare food", "Today"))
                dao.insertTaskCategoryCrossRef(TaskCategoryCrossRef("Call mom", "SomeDay"))
                dao.insertTaskCategoryCrossRef(TaskCategoryCrossRef("Visit grandma", "SomeDay"))
                dao.insertTaskCategoryCrossRef(TaskCategoryCrossRef("Repair my bike", "SomeDay"))
                dao.insertTaskCategoryCrossRef(TaskCategoryCrossRef("Call Elon Musk", "SomeDay"))
            }
        }
    }
}