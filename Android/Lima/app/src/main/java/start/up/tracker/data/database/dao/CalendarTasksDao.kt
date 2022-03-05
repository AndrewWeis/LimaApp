package start.up.tracker.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import start.up.tracker.data.entities.ExtendedTask

@Dao
interface CalendarTasksDao {

    @Query(
        """
       SELECT 
            task_table.taskId, task_table.taskName, task_table.taskDesc, task_table.priority,
            task_table.completed, task_table.created, task_table.dateLong, task_table.date,
            task_table.timeStart, task_table.timeEnd, task_table.timeStartInt, task_table.timeEndInt,
            task_table.wasCompleted,
	        Category.categoryId, Category.categoryName, Category.color, Category.tasksInside
        FROM cross_ref
        JOIN task_table ON task_table.taskId = cross_ref.taskId
        JOIN Category ON Category.categoryId = cross_ref.categoryId
       WHERE task_table.date = :today AND
       task_table.timeStart != "No time" AND
       task_table.timeEnd != "No time" AND
       (completed != :hideCompleted OR completed = 0)
       ORDER BY task_table.timeEndInt
       ASC
    """
    )
    fun getCalendarTasks(today: String, hideCompleted: Boolean): Flow<List<ExtendedTask>>
}
