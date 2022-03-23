package start.up.tracker.mvvm.view_models.categories

import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import start.up.tracker.database.dao.ProjectsDao
import start.up.tracker.entities.Project
import start.up.tracker.ui.data.constants.DEFAULT_PROJECT_COLOR
import start.up.tracker.utils.screens.StateHandleKeys
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val categoriesDao: ProjectsDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val category = state.get<Project>(StateHandleKeys.PROJECT_ID)
    var categoryName = category?.projectTitle ?: ""
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
        val newProject = Project(projectTitle = categoryName, color = color)
        categoriesDao.insertProject(newProject)
        addCategoryEventChannel.send(AddCategoryEvent.NavigateBack)
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addCategoryEventChannel.send(AddCategoryEvent.ShowInvalidInputMessage(text))
    }

    sealed class AddCategoryEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddCategoryEvent()
        object NavigateBack : AddCategoryEvent()
    }
}
