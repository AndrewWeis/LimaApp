package start.up.tracker.data

import androidx.lifecycle.LiveData
import androidx.room.*
import start.up.tracker.model.Task

@Dao
interface TaskDao {

    @Insert
    suspend fun addTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM task_table")
    suspend fun deleteAllTasks()

    @Query("SELECT * FROM task_table ORDER BY priority DESC")
    fun getAllTasks(): LiveData<List<Task>>



}