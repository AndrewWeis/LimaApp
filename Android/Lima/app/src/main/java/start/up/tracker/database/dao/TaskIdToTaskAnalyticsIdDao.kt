package start.up.tracker.database.dao

import androidx.room.*
import start.up.tracker.entities.TaskIdToTaskAnalyticsId

@Dao
interface TaskIdToTaskAnalyticsIdDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElement(taskIdToTaskAnalyticsIdDao: TaskIdToTaskAnalyticsId)

    @Update
    suspend fun updateElement(taskIdToTaskAnalyticsIdDao: TaskIdToTaskAnalyticsId)

    @Delete
    suspend fun deleteElement(taskIdToTaskAnalyticsIdDao: TaskIdToTaskAnalyticsId)

    @Query(
        "SELECT * FROM taskIdToTaskAnalyticsId_table"
    )
    suspend fun getAllElements(): List<TaskIdToTaskAnalyticsId>
}