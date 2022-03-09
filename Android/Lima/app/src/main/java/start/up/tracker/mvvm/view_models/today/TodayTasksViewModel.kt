package start.up.tracker.mvvm.view_models.today

import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import start.up.tracker.data.analytics.Analytics
import start.up.tracker.data.database.PreferencesManager
import start.up.tracker.data.database.dao.CategoriesDao
import start.up.tracker.data.database.dao.TaskDao
import start.up.tracker.data.database.dao.TodayTasksDao
import start.up.tracker.data.entities.Task
import start.up.tracker.mvvm.view_models.tasks.base.BaseTasksOperationsViewModel
import start.up.tracker.utils.ExtendedTasksMergeFlows.mergeFlowsForExtendedTask
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TodayTasksViewModel @Inject constructor(
    taskDao: TaskDao,
    preferencesManager: PreferencesManager,
    analytics: Analytics,
    todayTasksDao: TodayTasksDao,
    categoriesDao: CategoriesDao
) : BaseTasksOperationsViewModel(taskDao, preferencesManager, analytics) {

    private val formatter = SimpleDateFormat("dd.MM.yyyy")
    private val currentDate: String = formatter.format(Date())

    private val todayTasksFlow = todayTasksDao.getTodayTasks(currentDate)
    private val categoriesFlow = categoriesDao.getCategories()

    private val tasksFlow: Flow<List<Task>> = combine(
        hideCompleted,
        todayTasksFlow,
        categoriesFlow,
        ::mergeFlowsForExtendedTask
    )

    val todayTasks = tasksFlow.asLiveData()
}
