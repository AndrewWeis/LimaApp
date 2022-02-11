package start.up.tracker.mvvm.view_models

import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import start.up.tracker.data.constants.ADD_TASK_RESULT_OK
import start.up.tracker.data.database.dao.TaskDao
import start.up.tracker.data.entities.Category
import start.up.tracker.data.constants.DEFAULT_PROJECT_COLOR
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val taskDao: TaskDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val category = state.get<Category>("category")

    var categoryName = state.get<String>("categoryName") ?: category?.categoryName ?: ""
        set(value) {
            field = value
            state.set("categoryName", value)
        }

    var color: Int = state.get<Int>("color") ?: category?.color ?: DEFAULT_PROJECT_COLOR
        set(value) {
            field = value
            state.set("color", value)
        }


    private val addCategoryEventChannel = Channel<AddCategoryEvent>()
    val addCategoryEvent = addCategoryEventChannel.receiveAsFlow()


    fun onSaveClick() {
        if (categoryName.isBlank()) {
            showInvalidInputMessage("Label cannot be empty")
            return
        }

        val newCategory = Category(categoryName = categoryName, color = color)
        createCategory(newCategory)
    }

    private fun createCategory(category: Category) = viewModelScope.launch {
        taskDao.insertCategory(category)
        addCategoryEventChannel.send(AddCategoryEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addCategoryEventChannel.send(AddCategoryEvent.ShowInvalidInputMessage(text))
    }

    sealed class AddCategoryEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddCategoryEvent()
        data class NavigateBackWithResult(val result: Int) : AddCategoryEvent()
    }
}