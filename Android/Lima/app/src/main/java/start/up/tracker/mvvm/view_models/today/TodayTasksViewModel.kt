package start.up.tracker.mvvm.view_models.today

import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import start.up.tracker.data.analytics.Analytics
import start.up.tracker.data.database.PreferencesManager
import start.up.tracker.data.database.dao.TaskDao
import start.up.tracker.data.database.dao.TodayTasksDao
import start.up.tracker.mvvm.view_models.tasks.base.BaseTasksOperationsViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TodayTasksViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val todayTasksDao: TodayTasksDao,
    private val preferencesManager: PreferencesManager,
    private val analytics: Analytics
) : BaseTasksOperationsViewModel(taskDao, preferencesManager, analytics) {

    private val formatter = SimpleDateFormat("dd.MM.yyyy")
    private val currentDate: String = formatter.format(Date())

    private val todayTasksFlow = hideCompleted.flatMapLatest {
        todayTasksDao.getTodayTasks(currentDate, it ?: false)
    }
    val todayTasks = todayTasksFlow.asLiveData()
}
