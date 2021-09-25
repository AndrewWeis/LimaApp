package start.up.tracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [Task::class],
    version = 1,
    exportSchema = false
)

abstract class TaskDatabase: RoomDatabase() {

    abstract fun getTaskDao(): TaskDao

    companion object {
        const val DB_NAME = "task_database"
    }
}