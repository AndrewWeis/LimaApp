package start.up.tracker.mvvm.view_models.upcoming

import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import start.up.tracker.analytics.Analytics
import start.up.tracker.database.PreferencesManager
import start.up.tracker.database.dao.CategoriesDao
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.database.dao.UpcomingTasksDao
import start.up.tracker.entities.Task
import start.up.tracker.mvvm.view_models.tasks.base.BaseTasksOperationsViewModel
import start.up.tracker.utils.ExtendedTasksMergeFlows
import java.util.*
import javax.inject.Inject

@HiltViewModel
class UpcomingViewModel @Inject constructor(
    taskDao: TaskDao,
    preferencesManager: PreferencesManager,
    analytics: Analytics,
    categoriesDao: CategoriesDao,
    upcomingTasksDao: UpcomingTasksDao,
) : BaseTasksOperationsViewModel(taskDao, preferencesManager, analytics) {

    private val currentDate = Date().time

    private val upcomingTasksFlow = upcomingTasksDao.getUpcomingTasks()
    private val categoriesFlow = categoriesDao.getCategories()

    private val tasksFlow: Flow<List<Task>> = combine(
        hideCompleted,
        upcomingTasksFlow,
        categoriesFlow,
        ExtendedTasksMergeFlows::mergeFlowsForExtendedTask
    )

    val upcomingTasks = tasksFlow.asLiveData()
}
