package start.up.tracker.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import start.up.tracker.entities.Task

@Dao
interface EisenhowerMatrixTasksDao {

    @Query("""
        SELECT * 
        FROM task_table 
        WHERE eisenhowerMatrix != 0 
        ORDER BY eisenhowerMatrix ASC, priority DESC 
    """)
    fun getEisenhowerTasks(): Flow<List<Task>>
}
