package start.up.tracker.mvvm.view_models.upcoming

import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import start.up.tracker.data.database.PreferencesManager
import start.up.tracker.data.database.dao.AnalyticsDao
import start.up.tracker.data.database.dao.CrossRefDao
import start.up.tracker.data.database.dao.TaskDao
import start.up.tracker.data.database.dao.UpcomingTasksDao
import start.up.tracker.mvvm.view_models.tasks.base.BaseExtendedTasksViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class UpcomingViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val analyticsDao: AnalyticsDao,
    private val upcomingTasksDao: UpcomingTasksDao,
    private val crossRefDao: CrossRefDao,
    private val preferencesManager: PreferencesManager
) : BaseExtendedTasksViewModel(taskDao, crossRefDao, analyticsDao, preferencesManager) {

    private val currentDate = Date().time

    private val upcomingTasksFlow = hideCompleted.flatMapLatest {
        upcomingTasksDao.getUpcomingTasks(currentDate, it ?: false)
    }
    val upcomingTasks = upcomingTasksFlow.asLiveData()
}