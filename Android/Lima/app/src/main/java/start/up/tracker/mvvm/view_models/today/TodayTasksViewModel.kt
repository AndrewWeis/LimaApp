package start.up.tracker.mvvm.view_models.today

import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import start.up.tracker.analytics.Analytics
import start.up.tracker.database.PreferencesManager
import start.up.tracker.database.dao.CategoriesDao
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.database.dao.TodayTasksDao
import start.up.tracker.entities.Task
import start.up.tracker.mvvm.view_models.tasks.base.BaseTasksOperationsViewModel
import start.up.tracker.utils.ExtendedTasksMergeFlows.mergeFlowsForExtendedTask
import start.up.tracker.utils.TimeHelper
import javax.inject.Inject

@HiltViewModel
class TodayTasksViewModel @Inject constructor(
    taskDao: TaskDao,
    preferencesManager: PreferencesManager,
    analytics: Analytics,
    todayTasksDao: TodayTasksDao,
    categoriesDao: CategoriesDao
) : BaseTasksOperationsViewModel(taskDao, preferencesManager, analytics) {

    private val todayTasksFlow = todayTasksDao.getTodayTasks(
        TimeHelper.getCurrentDayInMilliseconds()
    )
    private val categoriesFlow = categoriesDao.getCategories()

    private val tasksFlow: Flow<List<Task>> = combine(
        hideCompleted,
        todayTasksFlow,
        categoriesFlow,
        ::mergeFlowsForExtendedTask
    )

    val todayTasks = tasksFlow.asLiveData()
}
