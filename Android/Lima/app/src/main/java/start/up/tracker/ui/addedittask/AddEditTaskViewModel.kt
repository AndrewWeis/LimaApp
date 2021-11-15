package start.up.tracker.ui.addedittask

import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import start.up.tracker.data.db.Task
import start.up.tracker.data.db.TaskDao
import start.up.tracker.data.db.relations.TaskCategoryCrossRef
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

    var taskImportance = state.get<Boolean>("taskImportance") ?: task?.important ?: false
        set(value) {
            field = value
            state.set("taskImportance", value)
        }

    //TODO(REPLACE WITH LIST OF CATEGORIES
    val categoryName = state.get<String>("categoryName") ?: ""

    // TODO(What will be if we add new category or edit exciting one? Does it replace or just add the new one)
    val categories = taskDao.getCategories()

    private val categoriesOfTask = taskDao.getCategoriesOfTask(task?.taskName ?: "")

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

    /**
     * This variable stores all categories and categories of the specific task.
     */
    val combinedCategories = runBlocking {
        categories.combine(categoriesOfTask) { set, subset ->
            Pair(set, subset?.categories)
        }
    }.asLiveData()


    fun onSaveClick() {
        if (taskName.isBlank()) {
            showInvalidInputMessage("Label cannot be empty")
            return
        }

        // TODO(It can be done better if I could just update CrossRef entity)
        val newCrossRef = TaskCategoryCrossRef(taskName = taskName, categoryName = categoryName)
        createCrossRef(newCrossRef)

        if (task != null) {
            // edit exciting task mode
            deleteCrossRefByTaskName(task.taskName)

            val updatedTask = task.copy(taskName = taskName, important = taskImportance)
            updatedTask(updatedTask)
        } else {
            // create new task mode
            val newTask = Task(taskName = taskName, important = taskImportance)
            createTask(newTask)
        }
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