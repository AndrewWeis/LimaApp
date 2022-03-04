package start.up.tracker.mvvm.view_models.tasks.base

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import start.up.tracker.data.database.PreferencesManager
import start.up.tracker.data.database.dao.AnalyticsDao
import start.up.tracker.data.database.dao.CrossRefDao
import start.up.tracker.data.database.dao.TaskDao
import start.up.tracker.data.entities.ExtendedTask
import start.up.tracker.data.entities.Task
import start.up.tracker.data.relations.TaskCategoryCrossRef
import start.up.tracker.ui.data.entities.TasksEvent
import start.up.tracker.utils.toTask

abstract class BaseExtendedTasksViewModel(
    private val taskDao: TaskDao,
    private val crossRefDao: CrossRefDao,
    private val analyticsDao: AnalyticsDao,
    private val preferencesManager: PreferencesManager,
) : BaseTasksEventsViewModel(analyticsDao, preferencesManager) {

    fun onUndoDeleteExtendedTaskClick(extendedTask: ExtendedTask) = viewModelScope.launch {
        val categoryId = extendedTask.categoryId
        val task = extendedTask.toTask()
        val crossRef = TaskCategoryCrossRef(task.taskId, categoryId)

        crossRefDao.insertTaskCategoryCrossRef(crossRef)
        taskDao.insertTask(task)
    }

    fun onExtendedTaskSelected(extendedTask: ExtendedTask) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToEditExtendedTaskScreen(extendedTask))
    }

    fun onExtendedTaskCheckedChanged(
        extendedTask: ExtendedTask,
        isChecked: Boolean
    ) = viewModelScope.launch {
        val task = extendedTask.toTask()
        if (isChecked && !task.wasCompleted) {
            addTaskToStat()
        }
        taskDao.updateTask(task.copy(completed = isChecked, wasCompleted = true))
    }

    fun onExtendedTaskSwiped(extendedTask: ExtendedTask) = viewModelScope.launch {
        val task = extendedTask.toTask()

        crossRefDao.deleteCrossRefByTaskId(task.taskId)
        taskDao.deleteTask(task)
        tasksEventChannel.send(TasksEvent.ShowUndoDeleteExtendedTaskMessage(extendedTask))
    }

    fun onTaskSwiped(task: Task) = viewModelScope.launch {
        crossRefDao.deleteCrossRefByTaskId(task.taskId)
        taskDao.deleteTask(task)
        tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
    }
}
