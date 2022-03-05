package start.up.tracker.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import start.up.tracker.data.entities.ExtendedTask

@Dao
interface TodayTasksDao {

    @Query(
        """
        SELECT COUNT(*)
        FROM task_table
        WHERE date = :today AND
        completed = 0
    """
    )
    fun countTodayTasks(today: String): Flow<Int>

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
       (completed != :hideCompleted OR completed = 0)
       ORDER BY priority 
       ASC
    """
    )
    fun getTodayTasks(today: String, hideCompleted: Boolean): Flow<List<ExtendedTask>>
}
