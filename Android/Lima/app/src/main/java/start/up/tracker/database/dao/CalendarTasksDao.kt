package start.up.tracker.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import start.up.tracker.entities.Task

@Dao
interface CalendarTasksDao {

    @Query(
        """
       SELECT *
       FROM task_table
       JOIN categories_table ON task_table.categoryId = categories_table.categoryId
       WHERE (completed != :hideCompleted OR completed = 0) AND
       task_table.date = :today AND
       task_table.startTimeInMinutes != null AND
       task_table.endTimeInMinutes != null
       ORDER BY task_table.endTimeInMinutes
       ASC
    """
    )
    fun getCalendarTasks(today: Long, hideCompleted: Boolean): Flow<List<Task>>
}
