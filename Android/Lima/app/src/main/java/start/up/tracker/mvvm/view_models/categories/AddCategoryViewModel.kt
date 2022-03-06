package start.up.tracker.mvvm.view_models.categories

import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import start.up.tracker.data.constants.ADD_RESULT_OK
import start.up.tracker.data.constants.DEFAULT_PROJECT_COLOR
import start.up.tracker.data.database.dao.CategoriesDao
import start.up.tracker.data.entities.Category
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val categoriesDao: CategoriesDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val category = state.get<Category>("category")
    var categoryName = category?.categoryName ?: ""
    var color = category?.color ?: DEFAULT_PROJECT_COLOR

    private val addCategoryEventChannel = Channel<AddCategoryEvent>()
    val addCategoryEvent = addCategoryEventChannel.receiveAsFlow()

    fun onSaveClick() {
        if (!isValidationSucceed()) {
            return
        }

        createCategory()
    }

    private fun isValidationSucceed(): Boolean {
        if (categoryName.isBlank()) {
            showInvalidInputMessage("Label cannot be empty")
            return false
        }

        return true
    }

    private fun createCategory() = viewModelScope.launch {
        val newCategory = Category(categoryName = categoryName, color = color)
        categoriesDao.insertCategory(newCategory)
        addCategoryEventChannel.send(AddCategoryEvent.NavigateBackWithResult(ADD_RESULT_OK))
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addCategoryEventChannel.send(AddCategoryEvent.ShowInvalidInputMessage(text))
    }

    sealed class AddCategoryEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddCategoryEvent()
        data class NavigateBackWithResult(val result: Int) : AddCategoryEvent()
    }
}
