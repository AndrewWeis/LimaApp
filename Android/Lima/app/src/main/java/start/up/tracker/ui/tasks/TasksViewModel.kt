package start.up.tracker.ui.tasks

import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.hilt.Assisted
import androidx.lifecycle.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import start.up.tracker.data.db.PreferencesManager
import start.up.tracker.data.db.SortOrder
import start.up.tracker.data.db.Task
import start.up.tracker.data.db.TaskDao
import start.up.tracker.data.db.models.Category
import start.up.tracker.ui.ADD_TASK_RESULT_OK
import start.up.tracker.ui.EDIT_TASK_RESULT_OK
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val searchQuery = state.getLiveData("searchQuery", "")

    val preferencesFlow = preferencesManager.preferencesFlow

    private val tasksEventChannel = Channel<TasksEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()

    private val tasksFlow = combine(
        searchQuery.asFlow(),
        preferencesFlow
    ) { query, filterPreferences ->
        Pair(query, filterPreferences)
    }.flatMapLatest { (query, filterPreferences) ->
        taskDao.getTasks(query, filterPreferences.sortOrder, filterPreferences.hideCompleted)
    }

    val tasks = tasksFlow.asLiveData()

    /**
     * Receive specific category either from [SavedStateHandle] in case app killed our app or from [SaveArgs]
     */
    val category = state.get<Category>("category")
    var categoryName = state.get<String>("categoryName") ?: category?.categoryName ?: ""
        set(value) {
            field = value
            state.set("categoryName", value)
        }

    /**
     * Transforms LiveData<List<CategoryWithTask>> to LiveData<List<Task>>
     * We need this transformation for passing all tasks of specific category to [TaskAdapter]
     */
    /*private val categoryWithTasks = taskDao.getTasksOfCategory(categoryName)
    val tasksOfCategory: LiveData<List<Task>> =
        Transformations.map(categoryWithTasks) { taskList ->
            var newData: List<Task> = listOf()
            taskList?.forEach {
                newData = it.tasks
            }
            return@map newData
        }*/

    private val tasksOfCategoryFlow = combine(
        searchQuery.asFlow(),
        preferencesFlow
    ) { query, filterPreferences ->
        Pair(query, filterPreferences)
    }.flatMapLatest { (query, filterPreferences) ->
        taskDao.getTasksOfCategory(query, filterPreferences.sortOrder, filterPreferences.hideCompleted, categoryName)
    }

    val tasksOfCategory = tasksOfCategoryFlow.asLiveData()



    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onHideCompletedClick(hideCompleted: Boolean) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

    fun onTaskSelected(task: Task) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToEditTaskScreen(task))
    }

    fun onTaskCheckedChanged(task: Task, isChecked: Boolean) = viewModelScope.launch {
        taskDao.updateTask(task.copy(completed = isChecked))
    }

    fun onTaskSwiped(task: Task) = viewModelScope.launch {
        taskDao.deleteTask(task)
        tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
    }

    fun onUndoDeleteClick(task: Task) = viewModelScope.launch {
        taskDao.insertTask(task)
    }

    fun onAddNewTaskClick() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToAddTaskScreen)
    }

    fun onAddEditResult(result: Int) {
        when(result) {
            ADD_TASK_RESULT_OK -> showTaskSavedConfirmationMessage("Task added")
            EDIT_TASK_RESULT_OK -> showTaskSavedConfirmationMessage("Task Updated")
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
        data class NavigateToEditTaskScreen(val task: Task) : TasksEvent()
        data class ShowUndoDeleteTaskMessage(val task: Task) : TasksEvent()
        data class ShowTaskSavedConfirmationMessage(val msg: String) : TasksEvent()
        object NavigateToDeleteAllCompletedScreen : TasksEvent()
    }
}