package start.up.tracker.database.dao

import androidx.room.*
import start.up.tracker.entities.Task
import start.up.tracker.entities.TaskAnalytics

@Dao
interface TaskAnalyticsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskAnalytics(taskAnalyticsDao: TaskAnalytics)

    @Update
    suspend fun updateTaskAnalytics(taskAnalyticsDao: TaskAnalytics)

    @Delete
    suspend fun deleteTaskAnalytics(taskAnalyticsDao: TaskAnalytics)

    @Query(
        """
            SELECT * 
            FROM taskAnalytics_table
            WHERE taskAnalytics_table.id = :id
           """
    )
    suspend fun getTaskById(id: Int): TaskAnalytics

    @Query(
        """
            SELECT * 
            FROM task_table
            WHERE date =:day
           """
    )
    // TODO подправить (мне), если надо
    suspend fun getTasksOfDay(day: Long): List<Task>

    @Query(
        """
            SELECT * 
            FROM taskAnalytics_table
           """
    )
    suspend fun getAllTasks(): List<TaskAnalytics>

    @Query(
        """
        SELECT COUNT(*) 
        FROM taskAnalytics_table
    """
    )
    suspend fun countTasksAnalytics(): Int
}