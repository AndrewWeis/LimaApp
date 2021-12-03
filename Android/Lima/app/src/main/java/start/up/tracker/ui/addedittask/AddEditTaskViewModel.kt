package start.up.tracker.ui.addedittask

import androidx.hilt.Assisted
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import start.up.tracker.data.models.Task
import start.up.tracker.data.db.TaskDao
import start.up.tracker.data.models.Category
import start.up.tracker.data.relations.TaskCategoryCrossRef
import start.up.tracker.ui.ADD_TASK_RESULT_OK
import start.up.tracker.ui.EDIT_TASK_RESULT_OK
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val taskDao: TaskDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    // Task stays the same because we have val fields in [Task]
    val task = state.get<Task>("task")

    var taskName = state.get<String>("taskName") ?: task?.taskName ?: ""
        set(value) {
            field = value
            state.set("taskName", value)
        }

    var taskDate = state.get<String>("taskDate") ?: task?.date ?: "No date"
        set(value) {
            field = value
            state.set("taskDate", value)
        }

    var taskPriority = state.get<Int>("taskPriority") ?: task?.priority ?: 1
        set(value) {
            field = value
            state.set("taskPriority", value)
        }

    val categoryName = state.get<String>("categoryName") ?: ""
    val categories = taskDao.getCategories()

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()


    /**
     * This variable stores all categories and categories of the specific task.
     */
    val combinedCategories = runBlocking {
        categories.combine(listOf(Category(categoryName)).asFlow()) { set, subset ->
            Pair(set, subset)
        }
    }.asLiveData()


    fun onSaveClick(checkedChip: String, date: String, priority: Int) {
        if (taskName.isBlank()) {
            showInvalidInputMessage("Label cannot be empty")
            return
        }

        if (task != null) { // edit exciting task mode
            deleteCrossRefByTaskName(task.taskName)

            val updatedTask = task.copy(taskName = taskName, priority = priority, date = date)
            updatedTask(updatedTask)
        } else { // create new task mode
            val newTask = Task(taskName = taskName, priority = priority, date = date)
            createTask(newTask)
        }

        // TODO(It can be done better if I could just update CrossRef entity)
        val newCrossRef = TaskCategoryCrossRef(taskName = taskName, categoryName = checkedChip)
        createCrossRef(newCrossRef)
    }

    private fun createCrossRef(CrossRef: TaskCategoryCrossRef) = viewModelScope.launch {
        taskDao.insertTaskCategoryCrossRef(CrossRef)
    }

    private fun deleteCrossRefByTaskName(taskName: String) = viewModelScope.launch {
        taskDao.deleteCrossRefByTaskName(taskName)
    }

    private fun createTask(task: Task) = viewModelScope.launch {
        taskDao.insertTask(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))
    }

    private fun updatedTask(task: Task) = viewModelScope.launch {
        taskDao.updateTask(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_TASK_RESULT_OK))
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(text))
    }

    sealed class AddEditTaskEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditTaskEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()
    }
}