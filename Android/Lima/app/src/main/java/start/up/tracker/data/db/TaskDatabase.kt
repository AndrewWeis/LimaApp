package start.up.tracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import start.up.tracker.data.models.Category
import start.up.tracker.data.models.DayStat
import start.up.tracker.data.models.Task
import start.up.tracker.data.relations.TaskCategoryCrossRef
import start.up.tracker.di.ApplicationScope
import javax.inject.Inject
import javax.inject.Provider


@Database(entities = [Task::class, Category::class, TaskCategoryCrossRef::class, DayStat::class], version = 1)
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

                dao.insertDayStat(DayStat(1, 5,1, 2021, 5))
                dao.insertDayStat(DayStat(2, 13,4, 2021, 8))
                dao.insertDayStat(DayStat(3, 8,6, 2021, 11))
                dao.insertDayStat(DayStat(4, 5,6, 2021, 24))
                dao.insertDayStat(DayStat(5, 7,7, 2021, 5))
                dao.insertDayStat(DayStat(6, 12,8, 2021, 5))
                dao.insertDayStat(DayStat(7, 18,11, 2021, 5))
                dao.insertDayStat(DayStat(8, 19,10, 2021, 5))
                dao.insertDayStat(DayStat(9, 25,12, 2021, 5))

                dao.insertDayStat(DayStat(10, 1,12, 2021, 12))
                dao.insertDayStat(DayStat(11, 3,12, 2021, 10))
                dao.insertDayStat(DayStat(12, 6,12, 2021, 4))
                dao.insertDayStat(DayStat(13, 8,12, 2021, 5))
                dao.insertDayStat(DayStat(14, 12,12, 2021, 6))
                dao.insertDayStat(DayStat(15, 14,12, 2021, 10))
                dao.insertDayStat(DayStat(16, 31,12, 2021, 9))
            }
        }
    }
}