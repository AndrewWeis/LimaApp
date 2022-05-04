package start.up.tracker.mvvm.view_models.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import start.up.tracker.analytics.ActiveAnalytics
import start.up.tracker.analytics.Analytics
import start.up.tracker.database.PreferencesManager
import start.up.tracker.database.dao.CalendarTasksDao
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.mvvm.view_models.tasks.base.BaseTasksOperationsViewModel
import start.up.tracker.ui.data.entities.calendar.CalendarTimesData
import start.up.tracker.utils.TimeHelper
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val calendarTasksDao: CalendarTasksDao,
    taskDao: TaskDao,
    preferencesManager: PreferencesManager,
    analytics: Analytics,
    activeAnalytics: ActiveAnalytics,
) : BaseTasksOperationsViewModel(taskDao, preferencesManager, analytics, activeAnalytics) {

    private val calendarTasksFlow = hideCompleted.flatMapLatest {
        calendarTasksDao.getCalendarTasks(
            hideCompleted = it,
            today = TimeHelper.getCurrentDayInMilliseconds()
        )
    }
    val calendarTasks = calendarTasksFlow.asLiveData()

    private val _calendarTimes: MutableLiveData<CalendarTimesData> = MutableLiveData()
    val calendarTimes: LiveData<CalendarTimesData> get() = _calendarTimes

    init {
        setupCalendarTimesData()
    }

    private fun setupCalendarTimesData() {
        val times: MutableList<String> = mutableListOf()

        if (TimeHelper.isSystem24Hour) {
            for (hour in 0..9) { times.add("0$hour:00") }
            for (hour in 10..23) { times.add("$hour:00") }
            times.add("00:00")
        } else {
            times.add("12:00 pm")
            for (hour in 1..12) { times.add("$hour:00 am") }
            for (hour in 1..12) { times.add("$hour:00 pm") }
            times.add("1:00 am")
        }

        _calendarTimes.postValue(CalendarTimesData(values = times))
    }
}
