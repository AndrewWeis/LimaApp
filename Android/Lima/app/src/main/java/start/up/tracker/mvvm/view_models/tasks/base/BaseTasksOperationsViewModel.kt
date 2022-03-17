package start.up.tracker.mvvm.view_models.tasks.base

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import start.up.tracker.analytics.Analytics
import start.up.tracker.database.PreferencesManager
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.entities.Task
import start.up.tracker.ui.data.entities.TasksEvent

abstract class BaseTasksOperationsViewModel(
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager,
    private val analytics: Analytics
) : BaseTasksEventsViewModel(preferencesManager) {

    fun onUndoDeleteTaskClick(task: Task) = viewModelScope.launch {
        taskDao.insertTask(task)
    }

    fun onTaskSelected(task: Task) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToEditTaskScreen(task))
    }

    fun onTaskCheckedChanged(
        task: Task,
    ) = viewModelScope.launch {
        if (task.completed && !task.wasCompleted) {
            analytics.addTaskToStatistic()
        }
        taskDao.updateTask(task.copy(wasCompleted = true))
    }

    fun onTaskSwiped(task: Task) = viewModelScope.launch {
        taskDao.deleteTask(task)
        taskDao.deleteSubtasks(task.taskId)
        tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
    }
}
