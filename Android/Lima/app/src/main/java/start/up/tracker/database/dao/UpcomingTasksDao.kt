package start.up.tracker.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import start.up.tracker.entities.Task

@Dao
interface UpcomingTasksDao {

    @Query(
        """
        SELECT *
        FROM task_table
        JOIN categories_table ON task_table.categoryId = categories_table.categoryId
        """
    )
    fun getUpcomingTasks(): Flow<List<Task>>

    @Query(
        """
        SELECT COUNT(*)
        FROM task_table
    """
    )
    fun countUpcomingTasks(): Flow<Int>
}
