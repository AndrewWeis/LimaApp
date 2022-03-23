package start.up.tracker.mvvm.view_models.tasks

import androidx.hilt.Assisted
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import start.up.tracker.analytics.ActiveAnalytics
import start.up.tracker.analytics.Analytics
import start.up.tracker.data.fields.Field
import start.up.tracker.data.fields.task.EditTaskInfoFieldSet
import start.up.tracker.database.dao.CategoriesDao
import start.up.tracker.database.dao.TaskAnalyticsDao
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.entities.Task
import start.up.tracker.entities.TaskAnalytics
import start.up.tracker.ui.data.constants.ADD_RESULT_OK
import start.up.tracker.ui.data.constants.EDIT_RESULT_OK
import start.up.tracker.utils.screens.StateHandleKeys
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val activeAnalytics: ActiveAnalytics,
    categoriesDao: CategoriesDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private var isEditMode = true

    private var task = state.get<Task>(StateHandleKeys.TASK) ?: Task()
    private val categoryId = state.get<Int>(StateHandleKeys.CATEGORY_ID) ?: -1

    private val _taskInfoLiveData: MutableLiveData<Task> = MutableLiveData()
    val taskInfoLiveData: LiveData<Task>
        get() = _taskInfoLiveData

    private val _titleField: MutableLiveData<Field<String>> = MutableLiveData()
    val titleField: LiveData<Field<String>>
        get() = _titleField

    val categories = categoriesDao.getCategories()

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

    private val fieldSet: EditTaskInfoFieldSet = EditTaskInfoFieldSet(task)

    init {
        if (task.title.isEmpty()) {
            isEditMode = false
        }

        showFields()
    }

    fun onSaveClick() {
        validateTitleField()

        if (!fieldSet.getTitleField().isValid()) {
            return
        }

        if (isEditMode) {
            updatedTask(task)
        } else {
            createTask(task)
        }
    }

    /**
     * Закончилось редактирование данных о задаче
     */
    fun onFinishedEditingTask() {
        showFields()
    }

    /**
     * Заголовок задачи был изменен
     *
     * @param title заголовок
     */
    fun onTaskTitleChanged(title: String) {
        fieldSet.onTitleChange(title.trim())
    }

    /**
     * Описание задачи было изменено
     *
     * @param description описание
     */
    fun onTaskDescriptionChanged(description: String) {
        task = task.copy(description = description.trim())
    }

    /**
     * Заголовок задачи был очищен
     */
    fun onTaskTitleClearClick() {
        fieldSet.onTitleChange("")
    }

    /**
     * Описание задачи было очищено
     */
    fun onTaskDescriptionClearClick() {
        task = task.copy(description = "")
    }

    /**
     * Время начала задачи было изменено
     *
     * @param minutes минуты
     */
    fun onTaskStartTimeChanged(minutes: Int) {
        task = task.copy(startTimeInMinutes = minutes)
        showFields()
    }

    /**
     * Время окончания задачи было изменено
     *
     * @param minutes минуты
     */
    fun onTaskEndTimeChanged(minutes: Int) {
        task = task.copy(endTimeInMinutes = minutes)
        showFields()
    }

    /**
     * Дата окончания задачи была изменена
     *
     * @param milliseconds дата в миллисекунды
     */
    fun onTaskDateChanged(milliseconds: Long) {
        task = task.copy(date = milliseconds)
        showFields()
    }

    private fun showFields() {
        showEditableTaskInfo()
        showTitleField()
    }

    private fun showEditableTaskInfo() {
        _taskInfoLiveData.postValue(task)
    }

    private fun showTitleField() {
        val field: Field<String> = fieldSet.getTitleField()
        _titleField.postValue(field)
    }

    private fun validateTitleField() {
        fieldSet.getTitleField().validate()

        if (fieldSet.getTitleField().isValid()) {
            task = task.copy(title = fieldSet.getTitleField().getValue()!!)
        }

        showTitleField()
    }

    private fun createTask(task: Task) = viewModelScope.launch {
        val taskId = taskDao.getTaskMaxId() ?: 0
        val newTask = task.copy(taskId = taskId + 1)
        activeAnalytics.managerAddTask(newTask)
        taskDao.insertTask(newTask)
        activeAnalytics.addTask(newTask)

        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_RESULT_OK))
    }

    private fun updatedTask(task: Task) = viewModelScope.launch {
        activeAnalytics.managerEditTask(task)
        taskDao.updateTask(task)
        activeAnalytics.editTask(task)

        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_RESULT_OK))
    }

    sealed class AddEditTaskEvent {
        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()
    }
}
