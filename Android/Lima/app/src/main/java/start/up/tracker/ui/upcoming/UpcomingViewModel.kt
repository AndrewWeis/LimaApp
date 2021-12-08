package start.up.tracker.ui.upcoming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import start.up.tracker.data.db.PreferencesManager
import start.up.tracker.data.db.TaskDao
import start.up.tracker.ui.base.BaseViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class UpcomingViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager
) : BaseViewModel(taskDao, preferencesManager) {

    private val currentDate = Date().time

    val tasksEvent = tasksEventBase
    val hideCompleted = hideCompletedBase

    private val upcomingTasksFlow = hideCompleted.flatMapLatest {
        taskDao.getUpcomingTasks(currentDate, it ?: false)
    }
    val upcomingTasks = upcomingTasksFlow.asLiveData()

}