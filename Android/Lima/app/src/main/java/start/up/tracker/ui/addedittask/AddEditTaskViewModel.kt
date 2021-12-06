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

    var taskTimeStart = state.get<String>("taskTimeStart") ?: task?.timeStart ?: "No Time"
        set(value) {
            field = value
            state.set("taskTimeStart", value)
        }

    var taskTimeEnd = state.get<String>("taskTimeEnd") ?: task?.timeEnd ?: "No Time"
        set(value) {
            field = value
            state.set("taskTimeEnd", value)
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

    fun onSaveClick(checkedChip: String, date: String, timeStart: String, timeEnd: String, priority: Int) {
        if (taskName.isBlank()) {
            showInvalidInputMessage("Label cannot be empty")
            return
        }

        if (task != null) { // edit exciting task mode
            deleteCrossRefByTaskName(task.taskName)

            val updatedTask = task.copy(taskName = taskName, priority = priority, date = date, timeStart = timeStart, timeEnd = timeEnd)
            updatedTask(updatedTask)
        } else { // create new task mode
            val newTask = Task(taskName = taskName, priority = priority, date = date, timeStart = timeStart, timeEnd = timeEnd)
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
            "05:00" -> 1
            "05:30" -> 2
            "06:00" -> 3
            "06:30" -> 4
            "07:00" -> 5
            "07:30" -> 6
            "08:00" -> 7
            "08:30" -> 8
            "09:00" -> 9
            "09:30" -> 10
            "10:00" -> 11
            "10:30" -> 12
            "11:00" -> 13
            "11:30" -> 14
            "12:00" -> 15
            "12:30" -> 16
            "13:00" -> 17
            "13:30" -> 18
            "14:00" -> 19
            "14:30" -> 20
            "15:00" -> 21
            "15:30" -> 22
            "16:00" -> 23
            "16:30" -> 24
            "17:00" -> 25
            "17:30" -> 26
            "18:00" -> 27
            "18:30" -> 28
            "19:00" -> 29
            "19:30" -> 30
            "20:00" -> 31
            "20:30" -> 32
            "21:00" -> 33
            "21:30" -> 34
            "22:00" -> 35
            "22:30" -> 36
            "23:00" -> 37
            "23:30" -> 38
            "00:00" -> 39
            "00:30" -> 40
            "01:00" -> 41
            "01:30" -> 42
            "02:00" -> 43
            "02:30" -> 44
            "03:00" -> 45
            "03:30" -> 46
            "04:00" -> 47
            "04:30" -> 48
            else -> 0
        }
    }
}