package start.up.tracker.mvvm.view_models

import androidx.hilt.Assisted
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import start.up.tracker.data.constants.ADD_TASK_RESULT_OK
import start.up.tracker.data.constants.EDIT_TASK_RESULT_OK
import start.up.tracker.data.database.PreferencesManager
import start.up.tracker.data.database.dao.AnalyticsDao
import start.up.tracker.data.database.dao.CrossRefDao
import start.up.tracker.data.database.dao.TaskDao
import start.up.tracker.data.entities.Category
import start.up.tracker.data.entities.DayStat
import start.up.tracker.data.entities.Task
import start.up.tracker.data.relations.TaskCategoryCrossRef
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProjectsTasksViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val analyticsDao: AnalyticsDao,
    private val crossRefDao: CrossRefDao,
    private val preferencesManager: PreferencesManager,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val searchQuery = state.getLiveData("searchQuery", "")

    private val tasksEventChannel = Channel<TasksEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()

    val hideCompleted = preferencesManager.hideCompleted

    private val calendar = Calendar.getInstance()
    private val currentYear: Int = calendar.get(Calendar.YEAR)
    private val currentMonth: Int = calendar.get(Calendar.MONTH) + 1
    private val currentDay: Int = calendar.get(Calendar.DAY_OF_MONTH)

    /**
     * Receive specific category either from [SavedStateHandle] in case app killed our app or from [SafeArgs]
     */
    val category = state.get<Category>("category")
    var categoryId = state.get<Int>("categoryId") ?: category?.categoryId ?: -1
        set(value) {
            field = value
            state.set("categoryId", value)
        }

    private val tasksOfCategoryFlow = combine(
        searchQuery.asFlow(),
        hideCompleted
    ) { query, hideCompleted ->
        Pair(query, hideCompleted)
    }.flatMapLatest { (query, hideCompleted) ->
        taskDao.getTasksOfCategory(query, hideCompleted ?: false, categoryId)
    }
    val tasksOfCategory = tasksOfCategoryFlow.asLiveData()

    fun onHideCompletedClick(hideCompleted: Boolean) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

    fun onTaskSelected(task: Task) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToEditTaskScreen(task))
    }

    fun onTaskCheckedChanged(task: Task, isChecked: Boolean) = viewModelScope.launch {
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

    fun onUndoDeleteClick(task: Task) = viewModelScope.launch {
        val crossRef = TaskCategoryCrossRef(task.taskId, categoryId)
        crossRefDao.insertTaskCategoryCrossRef(crossRef)
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

    private fun addTaskToStat() = viewModelScope.launch {
        var dayStat: DayStat? = analyticsDao.getStatDay(currentYear, currentMonth, currentDay)

        if (dayStat == null) {
            dayStat = DayStat(day = currentDay, month = currentMonth, year = currentYear)
            analyticsDao.insertDayStat(dayStat)
        } else {
            val newDayStat = dayStat.copy(completedTasks = dayStat.completedTasks + 1)
            analyticsDao.updateDayStat(newDayStat)
        }
    }

    sealed class TasksEvent {
        object NavigateToAddTaskScreen : TasksEvent()
        data class NavigateToEditTaskScreen(val task: Task) : TasksEvent()
        data class ShowUndoDeleteTaskMessage(val task: Task) : TasksEvent()
        data class ShowTaskSavedConfirmationMessage(val msg: String) : TasksEvent()
        object NavigateToDeleteAllCompletedScreen : TasksEvent()
    }
}
