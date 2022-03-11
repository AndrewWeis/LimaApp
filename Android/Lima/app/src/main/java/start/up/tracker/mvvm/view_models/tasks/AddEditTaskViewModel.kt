package start.up.tracker.mvvm.view_models.tasks

import androidx.hilt.Assisted
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import start.up.tracker.database.dao.CategoriesDao
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.entities.Task
import start.up.tracker.ui.data.constants.ADD_RESULT_OK
import start.up.tracker.ui.data.constants.EDIT_RESULT_OK
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val categoriesDao: CategoriesDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private var task = state.get<Task>("task") ?: Task()

    private val _taskLiveData = MutableLiveData(task)
    val taskLiveData: LiveData<Task>
        get() = _taskLiveData

    val categories = categoriesDao.getCategories()

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

    fun onSaveClick() {

    }

    /**
     * Выставляет значение task в taskLiveData
     */
    fun setTaskLiveDataValue() {
        _taskLiveData.postValue(task)
    }

    /**
     * Заголовок задачи был изменен
     *
     * @param title заголовок
     */
    fun onTaskTitleHasBeenChanged(title: String) {
        task = task.copy(title = title)
    }

    /**
     * Описание задачи было изменено
     *
     * @param description описание
     */
    fun onTaskDescriptionHasBeenChanged(description: String) {
        task = task.copy(description = description)
    }

    /**
     * Заголовок задачи был очищен
     */
    fun onTaskTitleClearClick() {
        task = task.copy(title = "")
    }

    /**
     * Описание задачи было очищено
     */
    fun onTaskDescriptionClearClick() {
        task = task.copy(description = "")
    }

    private fun createTask(task: Task, categoryName: String) = viewModelScope.launch {
        val categoryId = categoriesDao.getCategoryIdByName(categoryName)
        taskDao.insertTask(task.copy(categoryId = categoryId))
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_RESULT_OK))
    }

    private fun updatedTask(task: Task, categoryName: String) = viewModelScope.launch {
        val currCategoryId = categoriesDao.getCategoryIdByName(categoryName)
        taskDao.updateTask(task.copy(categoryId = currCategoryId))
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_RESULT_OK))
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(text))
    }

    sealed class AddEditTaskEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditTaskEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()
    }
}
