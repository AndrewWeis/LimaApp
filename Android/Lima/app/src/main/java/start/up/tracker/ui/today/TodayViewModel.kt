package start.up.tracker.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import start.up.tracker.data.db.PreferencesManager
import start.up.tracker.data.db.TaskDao
import start.up.tracker.data.models.ExtendedTask
import start.up.tracker.data.relations.TaskCategoryCrossRef
import start.up.tracker.ui.ADD_TASK_RESULT_OK
import start.up.tracker.ui.EDIT_TASK_RESULT_OK
import start.up.tracker.ui.base.BaseViewModel
import start.up.tracker.utils.toTask
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager,
) : BaseViewModel(taskDao, preferencesManager) {

    private val formatter = SimpleDateFormat("dd.MM.yyyy")
    private val currentDate: String = formatter.format(Date())

    val tasksEvent = tasksEventBase
    val hideCompleted = hideCompletedBase

    private val todayTasksFlow = hideCompleted.flatMapLatest {
        taskDao.getTodayTasks(currentDate, it ?: false)
    }
    val todayTasks = todayTasksFlow.asLiveData()


    private val calendarTasksFlow = hideCompleted.flatMapLatest {
        taskDao.getCalendarTasks(currentDate, it ?: false)
    }
    val calendarTasks = calendarTasksFlow.asLiveData()
}