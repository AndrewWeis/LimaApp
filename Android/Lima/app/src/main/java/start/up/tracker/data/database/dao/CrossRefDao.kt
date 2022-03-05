package start.up.tracker.data.database.dao

import androidx.room.*
import start.up.tracker.data.relations.TaskCategoryCrossRef

@Dao
interface CrossRefDao {

    @Query("DELETE FROM cross_ref WHERE taskId = :taskId")
    suspend fun deleteCrossRefByTaskId(taskId: Int?)

    @Query("SELECT * FROM cross_ref WHERE taskId = :taskId")
    suspend fun getCrossRefByTaskId(taskId: Int?): TaskCategoryCrossRef

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskCategoryCrossRef(crossRef: TaskCategoryCrossRef)

    @Update
    suspend fun updateCrossRef(crossRef: TaskCategoryCrossRef)
}
