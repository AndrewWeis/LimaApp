package start.up.tracker.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import start.up.tracker.data.db.Task
import start.up.tracker.repository.TaskRepositoryImpl

class TaskViewModel @ViewModelInject constructor(private val repository: TaskRepositoryImpl) : ViewModel() {

    suspend fun addTask(task: Task) = repository.addTask(task)

    suspend fun updateTask(task: Task) = repository.updateTask(task)

    suspend fun deleteTask(task: Task) = repository.deleteTask(task)

    suspend fun deleteAllTasks() = repository.deleteAllTasks()

    fun getAllTasks() = repository.getAllTasks()
}