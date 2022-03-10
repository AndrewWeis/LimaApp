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
       WHERE (completed != :hideCompleted OR completed = 0)
    """
    )
    fun getCalendarTasks(hideCompleted: Boolean): Flow<List<Task>>
}
