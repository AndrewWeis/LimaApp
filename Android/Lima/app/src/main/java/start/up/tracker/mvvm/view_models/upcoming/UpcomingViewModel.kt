package start.up.tracker.mvvm.view_models.upcoming

import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import start.up.tracker.data.analytics.Analytics
import start.up.tracker.data.database.PreferencesManager
import start.up.tracker.data.database.dao.TaskDao
import start.up.tracker.data.database.dao.UpcomingTasksDao
import start.up.tracker.mvvm.view_models.tasks.base.BaseTasksOperationsViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class UpcomingViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val upcomingTasksDao: UpcomingTasksDao,
    private val preferencesManager: PreferencesManager,
    private val analytics: Analytics
) : BaseTasksOperationsViewModel(taskDao, preferencesManager, analytics) {

    private val currentDate = Date().time

    private val upcomingTasksFlow = hideCompleted.flatMapLatest {
        upcomingTasksDao.getUpcomingTasks(currentDate, it ?: false)
    }
    val upcomingTasks = upcomingTasksFlow.asLiveData()
}
