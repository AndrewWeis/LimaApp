package start.up.tracker.mvvm.view_models.tasks

import androidx.hilt.Assisted
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import start.up.tracker.analytics.Analytics
import start.up.tracker.database.PreferencesManager
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.entities.Category
import start.up.tracker.mvvm.view_models.tasks.base.BaseTasksOperationsViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProjectsTasksViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager,
    private val analytics: Analytics,
    @Assisted private val state: SavedStateHandle
) : BaseTasksOperationsViewModel(taskDao, preferencesManager, analytics) {

    val searchQuery = state.getLiveData("searchQuery", "")

    /**
     * Receive specific category either from [SavedStateHandle] in case app killed our app or from [SafeArgs]
     */
    val category = state.get<Category>("category")
    var categoryId = state.get<Int>("categoryId") ?: category?.categoryId ?: -1
        set(value) {
            field = value
            state.set("categoryId", value)
        }

    private val tasksOfCategoryFlow = combine(
        searchQuery.asFlow(),
        hideCompleted
    ) { query, hideCompleted ->
        Pair(query, hideCompleted)
    }.flatMapLatest { (query, hideCompleted) ->
        taskDao.getTasksOfCategory(query, hideCompleted ?: false, categoryId)
    }
    val tasksOfCategory = tasksOfCategoryFlow.asLiveData()
}
