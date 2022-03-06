package start.up.tracker.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import start.up.tracker.data.entities.Task

@Dao
interface CalendarTasksDao {

    @Query(
        """
       SELECT *
       FROM task_table
       JOIN categories_table ON task_table.categoryId = categories_table.categoryId
       WHERE task_table.date = :today AND
       task_table.timeStart != "No time" AND
       task_table.timeEnd != "No time" AND
       (completed != :hideCompleted OR completed = 0)
       ORDER BY task_table.timeEndInt
       ASC
    """
    )
    fun getCalendarTasks(today: String, hideCompleted: Boolean): Flow<List<Task>>
}
