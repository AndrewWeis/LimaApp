package start.up.tracker.mvvm.view_models.esinhower_matrix

import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import start.up.tracker.analytics.ActiveAnalytics
import start.up.tracker.analytics.Analytics
import start.up.tracker.database.PreferencesManager
import start.up.tracker.database.dao.EisenhowerMatrixTasksDao
import start.up.tracker.database.dao.ProjectsDao
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.entities.Task
import start.up.tracker.mvvm.view_models.tasks.base.BaseTasksOperationsViewModel
import start.up.tracker.utils.ExtendedTasksMergeFlows
import javax.inject.Inject

@HiltViewModel
class EisenhowerMatrixViewModel @Inject constructor(
    taskDao: TaskDao,
    preferencesManager: PreferencesManager,
    analytics: Analytics,
    projectsDao: ProjectsDao,
    activeAnalytics: ActiveAnalytics,
    eisenhowerMatrixTasksDao: EisenhowerMatrixTasksDao

) : BaseTasksOperationsViewModel(taskDao, preferencesManager, analytics, activeAnalytics) {

    private val eisenhowerMatrixTasksFlow: Flow<List<Task>> =
        eisenhowerMatrixTasksDao.getEisenhowerTasks()

    private val projectsFlow = projectsDao.getProjects()

    private val tasksFlow: Flow<List<Task>> = combine(
        hideCompleted,
        eisenhowerMatrixTasksFlow,
        projectsFlow,
        ExtendedTasksMergeFlows::mergeFlowsForExtendedTask
    )

    val eisenhowerMatrixTasks = tasksFlow.asLiveData()
}
