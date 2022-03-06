package start.up.tracker.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import start.up.tracker.data.entities.Task

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
        SELECT *
        FROM task_table
        JOIN categories_table ON task_table.categoryId = categories_table.categoryId
        WHERE task_table.date = :today AND
       (completed != :hideCompleted OR completed = 0)
       ORDER BY priority 
       ASC
    """
    )
    fun getTodayTasks(today: String, hideCompleted: Boolean): Flow<List<Task>>
}
