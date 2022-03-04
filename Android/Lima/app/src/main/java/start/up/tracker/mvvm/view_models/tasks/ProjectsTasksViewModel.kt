package start.up.tracker.mvvm.view_models.tasks

import androidx.hilt.Assisted
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import start.up.tracker.data.database.PreferencesManager
import start.up.tracker.data.database.dao.AnalyticsDao
import start.up.tracker.data.database.dao.CrossRefDao
import start.up.tracker.data.database.dao.TaskDao
import start.up.tracker.data.entities.Category
import start.up.tracker.data.entities.Task
import start.up.tracker.data.relations.TaskCategoryCrossRef
import start.up.tracker.mvvm.view_models.tasks.base.BaseTasksViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProjectsTasksViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val analyticsDao: AnalyticsDao,
    private val crossRefDao: CrossRefDao,
    private val preferencesManager: PreferencesManager,
    @Assisted private val state: SavedStateHandle
) : BaseTasksViewModel(taskDao, crossRefDao, analyticsDao, preferencesManager) {

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

    fun onUndoDeleteTaskClick(task: Task) = viewModelScope.launch {
        val crossRef = TaskCategoryCrossRef(task.taskId, categoryId)
        crossRefDao.insertTaskCategoryCrossRef(crossRef)
        taskDao.insertTask(task)
    }
}
