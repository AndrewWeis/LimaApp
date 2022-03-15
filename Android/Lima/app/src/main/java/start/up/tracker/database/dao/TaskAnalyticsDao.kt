package start.up.tracker.database.dao

import androidx.room.*
import start.up.tracker.entities.TaskAnalytics
import java.util.ArrayList

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
            FROM taskAnalytics_table
           """
    )
    suspend fun getAllTasks(): List<TaskAnalytics>
}