package start.up.tracker.mvvm.view_models

import androidx.hilt.Assisted
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import start.up.tracker.data.constants.ADD_TASK_RESULT_OK
import start.up.tracker.data.constants.EDIT_TASK_RESULT_OK
import start.up.tracker.data.entities.Task
import start.up.tracker.data.database.dao.TaskDao
import start.up.tracker.data.entities.Category
import start.up.tracker.data.relations.TaskCategoryCrossRef
import start.up.tracker.utils.timeToMinutes
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

    var taskDesc = state.get<String>("taskDesc") ?: task?.taskDesc ?: ""
        set(value) {
            field = value
            state.set("taskDesc", value)
        }

    var taskDate = state.get<String>("taskDate") ?: task?.date ?: "No date"
        set(value) {
            field = value
            state.set("taskDate", value)
        }

    var taskDateLong = state.get<Long>("taskDateLong") ?: task?.dateLong ?: 0L
        set(value) {
            field = value
            state.set("taskDateLong", value)
        }

    var taskTimeStart = state.get<String>("taskTimeStart") ?: task?.timeStart ?: "No time"
        set(value) {
            field = value
            state.set("taskTimeStart", value)
        }

    var taskTimeEnd = state.get<String>("taskTimeEnd") ?: task?.timeEnd ?: "No time"
        set(value) {
            field = value
            state.set("taskTimeEnd", value)
        }

    var taskPriority = state.get<Int>("taskPriority") ?: task?.priority ?: 4
        set(value) {
            field = value
            state.set("taskPriority", value)
        }

    val categoryId = state.get<Int>("categoryId") ?: -1
    val categories = taskDao.getCategories()

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()


    /**
     * This variable stores all categories and categories of the specific task.
     */
    val combinedCategories = runBlocking {
        categories.combine(listOf(Category(categoryId = categoryId)).asFlow()) { set, subset ->
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

    fun onSaveClick(checkedChip: String, date: String, dateLong: Long, timeStart: String, timeEnd: String, priority: Int) {

        // ---------- VALIDATION START ----------

        if (taskName.isBlank()) {
            showInvalidInputMessage("Label cannot be empty")
            return
        }

        var timeStartInt = 0
        var timeEndInt = 0

        if (timeStart != "No time") {
            timeStartInt = timeToMinutes(timeStart)
            timeEndInt = timeToMinutes(timeEnd)
        }

        // ---------- VALIDATION END ----------

        if (task != null) { // edit exciting task mode
            val updatedTask = task.copy(taskName = taskName, taskDesc = taskDesc, priority = priority, date = date, dateLong = dateLong, timeStart = timeStart, timeEnd = timeEnd, timeStartInt = timeStartInt, timeEndInt = timeEndInt)
            updatedTask(updatedTask, checkedChip)
        } else { // create new task mode
            val newTask = Task(taskName = taskName, taskDesc = taskDesc, priority = priority, date = date, dateLong = dateLong, timeStart = timeStart, timeEnd = timeEnd, timeStartInt = timeStartInt, timeEndInt = timeEndInt)
            createTask(newTask, checkedChip)
        }

    }

    private fun createCrossRef(CrossRef: TaskCategoryCrossRef) = viewModelScope.launch {
        taskDao.insertTaskCategoryCrossRef(CrossRef)
    }

    private fun deleteCrossRefByTaskId(taskId: Int) = viewModelScope.launch {
        taskDao.deleteCrossRefByTaskId(taskId)
    }

    private fun createTask(task: Task, categoryName: String) = viewModelScope.launch {
        val categoryId = taskDao.getCategoryIdByName(categoryName)
        val taskId = taskDao.getTaskMaxId() ?: 0
        val newCrossRef = TaskCategoryCrossRef(taskId = taskId+1, categoryId = categoryId)

        taskDao.insertTask(task.copy(taskId = taskId+1))
        createCrossRef(newCrossRef)

        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))
    }

    private fun updatedTask(task: Task, categoryName: String) = viewModelScope.launch {

        val crossRef = taskDao.getCrossRefByTaskId(task.taskId)
        val currCategoryId = taskDao.getCategoryIdByName(categoryName)

        // if category has changed
        if (crossRef.categoryId != currCategoryId) {
            deleteCrossRefByTaskId(task.taskId)
            val newCrossRef = TaskCategoryCrossRef(taskId = task.taskId, categoryId = currCategoryId)
            createCrossRef(newCrossRef)
        }

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