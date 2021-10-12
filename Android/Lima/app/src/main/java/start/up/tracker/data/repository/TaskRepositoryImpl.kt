package start.up.tracker.data.repository

import start.up.tracker.data.db.Task
import start.up.tracker.data.db.TaskDao
import start.up.tracker.repository.TaskRepository
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao): TaskRepository {

    override suspend fun addTask(task: Task) = taskDao.insert(task)

    override suspend fun updateTask(task: Task) = taskDao.update(task)

    override suspend fun deleteTask(task: Task) = taskDao.delete(task)

    override suspend fun deleteAllTasks() = taskDao.deleteAllTasks()

    override fun getAllTasks() = taskDao.getAllTasks()
}