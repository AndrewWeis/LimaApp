package start.up.tracker.repository

import androidx.lifecycle.LiveData
import start.up.tracker.data.db.Task

interface TaskRepository {
    suspend fun addTask(task: Task);
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun deleteAllTasks()
    fun getAllTasks(): LiveData<List<Task>>
}