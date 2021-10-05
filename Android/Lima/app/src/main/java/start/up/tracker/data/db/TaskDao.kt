package start.up.tracker.data.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM task_table")
    suspend fun deleteAllTasks()

    @Query("SELECT * FROM task_table ORDER BY id DESC")
    fun getAllTasks(): LiveData<List<Task>>
}