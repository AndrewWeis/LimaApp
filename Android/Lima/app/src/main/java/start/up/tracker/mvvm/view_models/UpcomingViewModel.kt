package start.up.tracker.mvvm.view_models

import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import start.up.tracker.data.database.PreferencesManager
import start.up.tracker.data.database.dao.AnalyticsDao
import start.up.tracker.data.database.dao.TaskDao
import java.util.*
import javax.inject.Inject

@HiltViewModel
class UpcomingViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val analyticsDao: AnalyticsDao,
    private val preferencesManager: PreferencesManager
) : BaseViewModel(taskDao, analyticsDao, preferencesManager) {

    private val currentDate = Date().time

    val tasksEvent = tasksEventBase
    val hideCompleted = hideCompletedBase

    private val upcomingTasksFlow = hideCompleted.flatMapLatest {
        taskDao.getUpcomingTasks(currentDate, it ?: false)
    }
    val upcomingTasks = upcomingTasksFlow.asLiveData()
}
