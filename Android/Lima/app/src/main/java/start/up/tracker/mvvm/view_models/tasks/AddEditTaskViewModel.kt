package start.up.tracker.mvvm.view_models.tasks

import androidx.hilt.Assisted
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import start.up.tracker.data.fields.Field
import start.up.tracker.data.fields.task.EditTaskInfoFieldSet
import start.up.tracker.database.dao.CategoriesDao
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.entities.Task
import start.up.tracker.ui.data.constants.ADD_RESULT_OK
import start.up.tracker.ui.data.constants.EDIT_RESULT_OK
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val taskDao: TaskDao,
    categoriesDao: CategoriesDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private var isEditMode = true

    private var task = state.get<Task>("task") ?: Task()

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
            updatedTask(task, 1)
        } else {
            createTask(task, 1)
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

    private fun createTask(task: Task, categoryId: Int) = viewModelScope.launch {
        taskDao.insertTask(task.copy(categoryId = categoryId))
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_RESULT_OK))
    }

    private fun updatedTask(task: Task, categoryId: Int) = viewModelScope.launch {
        taskDao.updateTask(task.copy(categoryId = categoryId))
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_RESULT_OK))
    }

    sealed class AddEditTaskEvent {
        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()
    }
}
