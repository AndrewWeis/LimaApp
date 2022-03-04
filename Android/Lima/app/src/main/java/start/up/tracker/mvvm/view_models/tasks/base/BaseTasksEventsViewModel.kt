package start.up.tracker.mvvm.view_models.tasks.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import start.up.tracker.data.constants.ADD_TASK_RESULT_OK
import start.up.tracker.data.constants.EDIT_TASK_RESULT_OK
import start.up.tracker.data.database.PreferencesManager
import start.up.tracker.data.database.dao.AnalyticsDao
import start.up.tracker.data.entities.DayStat
import start.up.tracker.ui.data.entities.TasksEvent
import java.util.*

abstract class BaseTasksEventsViewModel(
    private val analyticsDao: AnalyticsDao,
    private val preferencesManager: PreferencesManager,
) : ViewModel() {

    val tasksEventChannel = Channel<TasksEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()
    val hideCompleted = preferencesManager.hideCompleted

    fun onHideCompletedClick(hideCompleted: Boolean) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

    fun onAddNewTaskClick() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToAddTaskScreen)
    }

    fun onDeleteAllCompletedClick() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToDeleteAllCompletedScreen)
    }

    fun onAddEditResult(result: Int) {
        when (result) {
            ADD_TASK_RESULT_OK -> showTaskSavedConfirmationMessage("Task added")
            EDIT_TASK_RESULT_OK -> showTaskSavedConfirmationMessage("Task updated")
        }
    }

    fun addTaskToStat() = viewModelScope.launch {
        val calendar = Calendar.getInstance()
        val currentYear: Int = calendar.get(Calendar.YEAR)
        val currentMonth: Int = calendar.get(Calendar.MONTH) + 1
        val currentDay: Int = calendar.get(Calendar.DAY_OF_MONTH)

        var dayStat: DayStat? = analyticsDao.getStatDay(currentYear, currentMonth, currentDay)

        if (dayStat == null) {
            dayStat = DayStat(day = currentDay, month = currentMonth, year = currentYear)
            analyticsDao.insertDayStat(dayStat)
        } else {
            val newDayStat = dayStat.copy(completedTasks = dayStat.completedTasks + 1)
            analyticsDao.updateDayStat(newDayStat)
        }
    }

    private fun showTaskSavedConfirmationMessage(text: String) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.ShowTaskSavedConfirmationMessage(text))
    }
}
