package start.up.tracker.mvvm.view_models.today

import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import start.up.tracker.data.database.PreferencesManager
import start.up.tracker.data.database.dao.*
import start.up.tracker.mvvm.view_models.tasks.base.BaseExtendedTasksViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val analyticsDao: AnalyticsDao,
    private val todayTasksDao: TodayTasksDao,
    private val calendarTasksDao: CalendarTasksDao,
    private val crossRefDao: CrossRefDao,
    private val preferencesManager: PreferencesManager,
) : BaseExtendedTasksViewModel(taskDao, crossRefDao, analyticsDao, preferencesManager) {

    private val formatter = SimpleDateFormat("dd.MM.yyyy")
    private val currentDate: String = formatter.format(Date())

    private val todayTasksFlow = hideCompleted.flatMapLatest {
        todayTasksDao.getTodayTasks(currentDate, it ?: false)
    }
    val todayTasks = todayTasksFlow.asLiveData()

    private val calendarTasksFlow = hideCompleted.flatMapLatest {
        calendarTasksDao.getCalendarTasks(currentDate, it ?: false)
    }
    val calendarTasks = calendarTasksFlow.asLiveData()
}
