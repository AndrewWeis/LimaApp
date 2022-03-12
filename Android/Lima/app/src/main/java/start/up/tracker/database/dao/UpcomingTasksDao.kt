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
        WHERE task_table.date > :today 
        ORDER BY date
        ASC
        """
    )
    fun getUpcomingTasks(today: Long): Flow<List<Task>>

    @Query(
        """
        SELECT COUNT(*)
        FROM task_table
        WHERE date > :today
    """
    )
    fun countUpcomingTasks(today: Long): Flow<Int>
}
