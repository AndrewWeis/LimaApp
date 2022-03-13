package start.up.tracker.mvvm.view_models.today

import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import start.up.tracker.analytics.Analytics
import start.up.tracker.database.PreferencesManager
import start.up.tracker.database.dao.CalendarTasksDao
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.mvvm.view_models.tasks.base.BaseTasksOperationsViewModel
import start.up.tracker.utils.TimeHelper
import javax.inject.Inject

@HiltViewModel
class CalendarTasksViewModel @Inject constructor(
    private val calendarTasksDao: CalendarTasksDao,
    taskDao: TaskDao,
    preferencesManager: PreferencesManager,
    analytics: Analytics,
) : BaseTasksOperationsViewModel(taskDao, preferencesManager, analytics) {

    private val calendarTasksFlow = hideCompleted.flatMapLatest {
        calendarTasksDao.getCalendarTasks(
            hideCompleted = it,
            today = TimeHelper.getCurrentDayInMilliseconds()
        )
    }
    val calendarTasks = calendarTasksFlow.asLiveData()
}
