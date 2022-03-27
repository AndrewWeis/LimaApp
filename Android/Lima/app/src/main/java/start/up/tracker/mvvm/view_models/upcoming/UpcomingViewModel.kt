package start.up.tracker.mvvm.view_models.upcoming

import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import start.up.tracker.analytics.ActiveAnalytics
import start.up.tracker.analytics.Analytics
import start.up.tracker.database.PreferencesManager
import start.up.tracker.database.dao.ProjectsDao
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.database.dao.UpcomingTasksDao
import start.up.tracker.entities.Task
import start.up.tracker.mvvm.view_models.tasks.base.BaseTasksOperationsViewModel
import start.up.tracker.utils.ExtendedTasksMergeFlows
import start.up.tracker.utils.TimeHelper
import javax.inject.Inject

@HiltViewModel
class UpcomingViewModel @Inject constructor(
    taskDao: TaskDao,
    preferencesManager: PreferencesManager,
    analytics: Analytics,
    projectsDao: ProjectsDao,
    upcomingTasksDao: UpcomingTasksDao,
    private val activeAnalytics: ActiveAnalytics,
) : BaseTasksOperationsViewModel(taskDao, preferencesManager, analytics, activeAnalytics) {

    private val upcomingTasksFlow = upcomingTasksDao.getUpcomingTasks(
        TimeHelper.getCurrentDayInMilliseconds()
    )
    private val projectsFlow = projectsDao.getProjects()

    private val tasksFlow: Flow<List<Task>> = combine(
        hideCompleted,
        upcomingTasksFlow,
        projectsFlow,
        ExtendedTasksMergeFlows::mergeFlowsForExtendedTask
    )

    val upcomingTasks = tasksFlow.asLiveData()
}
