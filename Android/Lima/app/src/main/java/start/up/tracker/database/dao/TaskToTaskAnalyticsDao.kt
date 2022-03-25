package start.up.tracker.database.dao

import androidx.room.*
import start.up.tracker.entities.TaskToTaskAnalytics

@Dao
interface TaskToTaskAnalyticsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElement(taskToTaskAnalyticsDao: TaskToTaskAnalytics)

    @Update
    suspend fun updateElement(taskToTaskAnalyticsDao: TaskToTaskAnalytics)

    @Delete
    suspend fun deleteElement(taskToTaskAnalyticsDao: TaskToTaskAnalytics)

    @Query(
        """
            SELECT * 
            FROM taskToTaskAnalytics_table
           """
    )
    suspend fun getAllElements(): List<TaskToTaskAnalytics>
}