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
       JOIN projects_table ON task_table.projectId = projects_table.projectId
       WHERE (completed != :hideCompleted OR completed = 0) AND
       task_table.date = :today AND
       task_table.startTimeInMinutes is not NULL AND
       task_table.endTimeInMinutes is not NULL
       ORDER BY task_table.endTimeInMinutes
       ASC
    """
    )
    fun getCalendarTasks(today: Long, hideCompleted: Boolean): Flow<List<Task>>
}
