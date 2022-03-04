package start.up.tracker.mvvm.view_models

import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import start.up.tracker.data.database.PreferencesManager
import start.up.tracker.data.database.dao.AnalyticsDao
import start.up.tracker.data.database.dao.CrossRefDao
import start.up.tracker.data.database.dao.TaskDao
import start.up.tracker.data.database.dao.UpcomingTasksDao
import java.util.*
import javax.inject.Inject

@HiltViewModel
class UpcomingViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val analyticsDao: AnalyticsDao,
    private val upcomingTasksDao: UpcomingTasksDao,
    private val crossRefDao: CrossRefDao,
    private val preferencesManager: PreferencesManager
) : BaseViewModel(taskDao, crossRefDao, analyticsDao, preferencesManager) {

    private val currentDate = Date().time

    val tasksEvent = tasksEventBase
    val hideCompleted = hideCompletedBase

    private val upcomingTasksFlow = hideCompleted.flatMapLatest {
        upcomingTasksDao.getUpcomingTasks(currentDate, it ?: false)
    }
    val upcomingTasks = upcomingTasksFlow.asLiveData()
}
