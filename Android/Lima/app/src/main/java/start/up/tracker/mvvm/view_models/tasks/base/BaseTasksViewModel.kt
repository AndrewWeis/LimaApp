package start.up.tracker.mvvm.view_models.tasks.base

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import start.up.tracker.data.database.PreferencesManager
import start.up.tracker.data.database.dao.AnalyticsDao
import start.up.tracker.data.database.dao.CrossRefDao
import start.up.tracker.data.database.dao.TaskDao
import start.up.tracker.data.entities.Task
import start.up.tracker.ui.data.entities.TasksEvent

abstract class BaseTasksViewModel(
    private val taskDao: TaskDao,
    private val crossRefDao: CrossRefDao,
    private val analyticsDao: AnalyticsDao,
    private val preferencesManager: PreferencesManager,
) : BaseTasksEventsViewModel(analyticsDao, preferencesManager) {

    // todo(figure out how to use categoryId here)
    /*fun onUndoDeleteTaskClick(task: Task) = viewModelScope.launch {
        val crossRef = TaskCategoryCrossRef(task.taskId, categoryId)
        crossRefDao.insertTaskCategoryCrossRef(crossRef)
        taskDao.insertTask(task)
    }*/

    fun onTaskSelected(task: Task) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToEditTaskScreen(task))
    }

    fun onTaskCheckedChanged(
        task: Task,
        isChecked: Boolean
    ) = viewModelScope.launch {
        if (isChecked && !task.wasCompleted) {
            addTaskToStat()
        }
        taskDao.updateTask(task.copy(completed = isChecked, wasCompleted = true))
    }

    fun onTaskSwiped(task: Task) = viewModelScope.launch {
        crossRefDao.deleteCrossRefByTaskId(task.taskId)
        taskDao.deleteTask(task)
        tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
    }
}
