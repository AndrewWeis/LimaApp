package start.up.tracker.mvvm.view_models.tasks.base

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import start.up.tracker.analytics.ActiveAnalytics
import start.up.tracker.analytics.Analytics
import start.up.tracker.database.PreferencesManager
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.entities.Task
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.TasksEvent

abstract class BaseTasksOperationsViewModel(
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager,
    val analytics: Analytics,
    private val activeAnalytics: ActiveAnalytics
) : BaseTasksEventsViewModel(preferencesManager) {

    fun onUndoDeleteTaskClick(task: Task, subtasks: List<Task>) = viewModelScope.launch {
        taskDao.insertTask(task)
        taskDao.insertSubtasks(subtasks)

        activeAnalytics.recoverTask(task)
    }

    fun onTaskSelected(task: Task) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToEditTaskScreen(task))
    }

    fun onTaskCheckedChanged(
        task: Task,
    ) = viewModelScope.launch {
        if (task.completed && !task.wasCompleted) {
            analytics.addTaskToStatisticOnCompletion()
        }
        taskDao.updateTask(task.copy(wasCompleted = true))

        activeAnalytics.updateStatus(task)
    }

    fun onTaskSwiped(listItem: ListItem) = viewModelScope.launch {
        val task = listItem.data as Task

        deleteTask(task)

        val subtaskToRestore = taskDao.getSubtasksToRestore(task.taskId)
        taskDao.deleteSubtasks(task.taskId)

        activeAnalytics.deleteTask(task)

        tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task, subtaskToRestore))
    }

    suspend fun deleteTask(task: Task) {
        //if (task.originalTaskId == -1) {
            taskDao.deleteTask(task)
        /*} else {
            val tasksOfHabit = taskDao.getTasksOfHabit(task.originalTaskId)
            for (taskOfHabit in tasksOfHabit) {
                taskDao.deleteTask(taskOfHabit)
            }
        }*/
    }
}
