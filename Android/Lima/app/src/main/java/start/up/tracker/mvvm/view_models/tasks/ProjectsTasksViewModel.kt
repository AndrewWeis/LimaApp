package start.up.tracker.mvvm.view_models.tasks

import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import start.up.tracker.analytics.Analytics
import start.up.tracker.database.PreferencesManager
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.mvvm.view_models.tasks.base.BaseTasksOperationsViewModel
import start.up.tracker.utils.screens.StateHandleKeys
import javax.inject.Inject

@HiltViewModel
class ProjectsTasksViewModel @Inject constructor(
    private val taskDao: TaskDao,
    @Assisted private val state: SavedStateHandle,
    preferencesManager: PreferencesManager,
    analytics: Analytics,
) : BaseTasksOperationsViewModel(taskDao, preferencesManager, analytics) {

    val searchQuery = state.getLiveData(StateHandleKeys.SEARCH_QUERY, "")
    var projectId = state.get<Int>(StateHandleKeys.PROJECT_ID) ?: -1

    private val projectTasksFlow = combine(
        searchQuery.asFlow(),
        hideCompleted
    ) { query, hideCompleted ->
        Pair(query, hideCompleted)
    }.flatMapLatest { (query, hideCompleted) ->
        taskDao.getTasksOfProject(query, hideCompleted, projectId)
    }

    val projectTasks = projectTasksFlow.asLiveData()
}
