package start.up.tracker.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import start.up.tracker.data.entities.Task

@Dao
interface UpcomingTasksDao {

    @Query(
        """
        SELECT *
        FROM task_table
        JOIN categories_table ON task_table.categoryId = categories_table.categoryId
        WHERE task_table.dateLong > :today AND
        (completed != :hideCompleted OR completed = 0)
        ORDER BY dateLong
        ASC
        """
    )
    fun getUpcomingTasks(today: Long, hideCompleted: Boolean): Flow<List<Task>>

    @Query(
        """
        SELECT COUNT(*)
        FROM task_table
        WHERE dateLong > :today AND
        completed = 0
    """
    )
    fun countUpcomingTasks(today: Long): Flow<Int>
}
