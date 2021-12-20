package start.up.tracker.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import start.up.tracker.data.db.PreferencesManager
import start.up.tracker.data.db.TaskDao
import start.up.tracker.data.models.DayStat
import start.up.tracker.data.models.ExtendedTask
import start.up.tracker.data.relations.TaskCategoryCrossRef
import start.up.tracker.ui.ADD_TASK_RESULT_OK
import start.up.tracker.ui.EDIT_TASK_RESULT_OK
import start.up.tracker.utils.toTask
import java.util.*

abstract class BaseViewModel(
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager,
) : ViewModel() {

    private val calendar = Calendar.getInstance()
    private val currentYear: Int = calendar.get(Calendar.YEAR)
    private val currentMonth: Int = calendar.get(Calendar.MONTH) + 1
    private val currentDay: Int = calendar.get(Calendar.DAY_OF_MONTH)

    private val tasksEventChannel = Channel<TasksEvent>()
    val tasksEventBase = tasksEventChannel.receiveAsFlow()

    val hideCompletedBase = preferencesManager.hideCompleted

    fun onHideCompletedClick(hideCompleted: Boolean) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

    fun onTaskSelected(extendedTask: ExtendedTask) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToEditTaskScreen(extendedTask))
    }

    fun onTaskCheckedChanged(extendedTask: ExtendedTask, isChecked: Boolean) = viewModelScope.launch {
        val task = extendedTask.toTask()
        if (isChecked && !task.wasCompleted) {
            addTaskToStat()
        }
        taskDao.updateTask(task.copy(completed = isChecked, wasCompleted = true))
    }

    private fun addTaskToStat() = viewModelScope.launch {
        var dayStat: DayStat? = taskDao.getStatDay(currentYear, currentMonth, currentDay)

        if (dayStat == null) {
            dayStat = DayStat(day = currentDay, month = currentMonth, year = currentYear)
            taskDao.insertDayStat(dayStat)
        } else {
            val newDayStat = dayStat.copy(completedTasks = dayStat.completedTasks + 1)
            taskDao.updateDayStat(newDayStat)
        }
    }

    fun onTaskSwiped(extendedTask: ExtendedTask) = viewModelScope.launch {
        val task = extendedTask.toTask()
        taskDao.deleteCrossRefByTaskId(task.taskId)
        taskDao.deleteTask(task)
        tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(extendedTask))
    }

    fun onUndoDeleteClick(extendedTask: ExtendedTask) = viewModelScope.launch {
        val categoryId = extendedTask.categoryId
        val task = extendedTask.toTask()
        val crossRef = TaskCategoryCrossRef(task.taskId, categoryId)
        taskDao.insertTaskCategoryCrossRef(crossRef)
        taskDao.insertTask(task)
    }

    fun onAddNewTaskClick() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToAddTaskScreen)
    }

    fun onAddEditResult(result: Int) {
        when(result) {
            ADD_TASK_RESULT_OK -> showTaskSavedConfirmationMessage("Task added")
            EDIT_TASK_RESULT_OK -> showTaskSavedConfirmationMessage("Task updated")
        }
    }

    private fun showTaskSavedConfirmationMessage(text: String) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.ShowTaskSavedConfirmationMessage(text))
    }

    fun onDeleteAllCompletedClick() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToDeleteAllCompletedScreen)
    }

    sealed class TasksEvent {
        object NavigateToAddTaskScreen : TasksEvent()
        data class NavigateToEditTaskScreen(val extendedTask: ExtendedTask) : TasksEvent()
        data class ShowUndoDeleteTaskMessage(val extendedTask: ExtendedTask) : TasksEvent()
        data class ShowTaskSavedConfirmationMessage(val msg: String) : TasksEvent()
        object NavigateToDeleteAllCompletedScreen : TasksEvent()
    }
}