package start.up.tracker.database.dao

import androidx.room.*
import start.up.tracker.entities.TaskAnalytics

@Dao
interface TaskAnalyticsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskAnalytics(taskAnalyticsDao: TaskAnalytics)

    @Update
    suspend fun updateTaskAnalytics(taskAnalyticsDao: TaskAnalytics)

    @Delete
    suspend fun deleteTaskAnalytics(taskAnalyticsDao: TaskAnalytics)
}