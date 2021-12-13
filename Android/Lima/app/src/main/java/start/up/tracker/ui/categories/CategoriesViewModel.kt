package start.up.tracker.ui.categories

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import start.up.tracker.data.db.TaskDao
import start.up.tracker.data.models.Category
import java.text.SimpleDateFormat
import java.util.*
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

    private val getInboxTasksCountFlow = taskDao.countTasksOfInbox()
    val getInboxTasksCount = getInboxTasksCountFlow.asLiveData()


    private val formatter = SimpleDateFormat("dd.MM.yyyy")
    private val currentDate: String = formatter.format(Date())
    private val currentDateLong = Date().time

    private val todayTasksCountFlow = taskDao.countTodayTasks(currentDate)
    val todayTasksCount = todayTasksCountFlow.asLiveData()

    private val upcomingTasksCountFlow = taskDao.countUpcomingTasks(currentDateLong)
    val upcomingTasksCount = upcomingTasksCountFlow.asLiveData()

    fun updateNumberOfTasks() = viewModelScope.launch {
        _categories.value?.forEach {
            val number = taskDao.countTasksOfCategory(it.categoryId, true)
            taskDao.updateCategory(it.copy(tasksInside = number))
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

    fun onTodaySelected() = viewModelScope.launch {
        categoryEventChannel.send(CategoryEvent.NavigateToToday)
    }

    fun onUpcomingSelected() = viewModelScope.launch {
        categoryEventChannel.send(CategoryEvent.NavigateToUpcoming)
    }

    sealed class CategoryEvent {
        data class NavigateToCategoryInside(val category: Category) : CategoryEvent()
        object NavigateToAddCategoryScreen : CategoryEvent()
        data class ShowCategorySavedConfirmationMessage(val msg: String) : CategoryEvent()
        object NavigateToToday : CategoryEvent()
        object NavigateToUpcoming : CategoryEvent()
    }
}