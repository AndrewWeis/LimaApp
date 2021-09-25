package start.up.tracker.repository

import start.up.tracker.data.db.Task
import start.up.tracker.data.db.TaskDao
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao): TaskRepository {

    override suspend fun addTask(task: Task) = taskDao.addTask(task)

    override suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    override suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    override suspend fun deleteAllTasks() = taskDao.deleteAllTasks()

    override fun getAllTasks() = taskDao.getAllTasks()
}