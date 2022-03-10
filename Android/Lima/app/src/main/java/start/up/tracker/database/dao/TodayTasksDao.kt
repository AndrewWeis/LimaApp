package start.up.tracker.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import start.up.tracker.entities.Task

@Dao
interface TodayTasksDao {

    @Query(
        """
        SELECT COUNT(*)
        FROM task_table
        WHERE completed = 0
    """
    )
    fun countTodayTasks(): Flow<Int>

    @Query(
        """
        SELECT *
        FROM task_table
        JOIN categories_table ON task_table.categoryId = categories_table.categoryId
    """
    )
    fun getTodayTasks(): Flow<List<Task>>
}
