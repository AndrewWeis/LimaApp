package start.up.tracker.repository

import androidx.lifecycle.LiveData
import start.up.tracker.data.TaskDao
import start.up.tracker.model.Task

class TaskRepository(private val taskDao: TaskDao) {

    val getAllTasks: LiveData<List<Task>> = taskDao.getAllTasks()

    suspend fun addTask(user: Task) {
        taskDao.addTask(user)
    }

    suspend fun updateTask(user: Task) {
        taskDao.updateTask(user)
    }

    suspend fun deleteTask(user: Task) {
        taskDao.deleteTask(user)
    }

    suspend fun deleteAllTasks() {
        taskDao.deleteAllTasks()
    }
}