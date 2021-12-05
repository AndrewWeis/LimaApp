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
import java.text.SimpleDateFormat
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

    var taskPriority = state.get<Int>("taskPriority") ?: task?.priority ?: 4
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


    fun priorityToInt(checkedPriority: String): Int {
        return when (checkedPriority) {
            "P1" -> 1
            "P2" -> 2
            "P3" -> 3
            "No priority" -> 4
            else -> -1
        }
    }

    fun formatToDate(it: Long?): String {
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
        return simpleDateFormat.format(it)
    }

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

    private fun convertTimeToInt(time: String): Int {
        return when(time) {
            "5:00" -> 1
            "5:30" -> 2
            "6:00" -> 3
            "6:30" -> 4
            "7:00" -> 5
            "7:30" -> 6
            "8:00" -> 7
            "8:30" -> 7
            "9:00" -> 8
            "9:30" -> 9
            "10:00" -> 10
            "10:30" -> 11
            "11:00" -> 12
            "11:30" -> 13
            "12:00" -> 14
            "12:30" -> 15
            "13:00" -> 16
            "13:30" -> 17
            "14:00" -> 18
            "14:30" -> 19
            "15:00" -> 20
            "15:30" -> 21
            "16:00" -> 22
            "16:30" -> 23
            "17:00" -> 24
            "17:30" -> 25
            "18:00" -> 26
            "18:30" -> 27
            "19:00" -> 28
            "19:30" -> 29
            "20:00" -> 30
            "20:30" -> 31
            "21:00" -> 32
            "21:30" -> 33
            "22:00" -> 34
            "22:30" -> 35
            "23:00" -> 36
            "23:30" -> 37
            "00:00" -> 38
            "00:30" -> 39
            "01:00" -> 40
            "01:30" -> 41
            "02:00" -> 42
            "02:30" -> 43
            "03:00" -> 44
            "03:30" -> 45
            "04:00" -> 46
            "04:30" -> 47
            else -> 0
        }
    }
}