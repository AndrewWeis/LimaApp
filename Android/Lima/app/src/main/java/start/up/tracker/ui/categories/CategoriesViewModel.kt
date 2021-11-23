package start.up.tracker.ui.categories

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import start.up.tracker.data.db.TaskDao
import start.up.tracker.data.db.models.Category
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val taskDao: TaskDao,
) : ViewModel() {

    private val _categories = taskDao.getCategories().asLiveData()

    val categories: LiveData<List<Category>> =
        Transformations.map(_categories) {
            val newData: MutableList<Category> = mutableListOf()
            it.forEach { category ->
                if (category.categoryName != "Inbox")
                    newData.add(category)
            }
            return@map newData
        }

    private val categoryEventChannel = Channel<CategoryEvent>()
    val categoryEvent = categoryEventChannel.receiveAsFlow()

    val inboxTasks: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>(0)
    }

    fun updateNumberOfTasks() = viewModelScope.launch {
        _categories.value?.forEach {
            val number = taskDao.countTasksOfCategory(it.categoryName, true)
            taskDao.updateCategory(it.copy(tasksInside = number))

            if (it.categoryName == "Inbox")
                inboxTasks.value = number
        }
    }

    fun onCategorySelected(category: Category) = viewModelScope.launch {
        categoryEventChannel.send(CategoryEvent.NavigateToCategoryInside(category))
    }

    fun onCategoryInboxSelected(category: Category) = viewModelScope.launch {
        categoryEventChannel.send(CategoryEvent.NavigateToCategoryInside(category))
    }

    fun onAddNewCategoryClick() = viewModelScope.launch {
        categoryEventChannel.send(CategoryEvent.NavigateToAddCategoryScreen)
    }

    fun showCategorySavedConfirmationMessage() = viewModelScope.launch {
        categoryEventChannel.send(CategoryEvent.ShowCategorySavedConfirmationMessage("Project added"))
    }

    sealed class CategoryEvent {
        data class NavigateToCategoryInside(val category: Category) : CategoryEvent()
        object NavigateToAddCategoryScreen : CategoryEvent()
        data class ShowCategorySavedConfirmationMessage(val msg: String) : CategoryEvent()
    }
}