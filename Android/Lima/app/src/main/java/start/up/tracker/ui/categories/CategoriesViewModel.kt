package start.up.tracker.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import start.up.tracker.data.db.TaskDao
import start.up.tracker.data.db.models.Category
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val taskDao: TaskDao,
) : ViewModel() {

    val categories = taskDao.getCategories().asLiveData()


    private val categoryEventChannel = Channel<CategoryEvent>()
    val categoryEvent = categoryEventChannel.receiveAsFlow()

    fun onCategorySelected(category: Category) = viewModelScope.launch {
        categoryEventChannel.send(CategoryEvent.NavigateToCategoryInside(category))
    }

    sealed class CategoryEvent {
        data class NavigateToCategoryInside(val category: Category) : CategoryEvent()
    }
}