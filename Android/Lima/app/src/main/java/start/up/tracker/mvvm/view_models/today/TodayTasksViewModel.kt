package start.up.tracker.mvvm.view_models.today

import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import start.up.tracker.data.database.PreferencesManager
import start.up.tracker.data.database.dao.*
import start.up.tracker.mvvm.view_models.tasks.base.BaseTasksOperationsViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TodayTasksViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val analyticsDao: AnalyticsDao,
    private val todayTasksDao: TodayTasksDao,
    private val preferencesManager: PreferencesManager,
) : BaseTasksOperationsViewModel(taskDao, analyticsDao, preferencesManager) {

    private val formatter = SimpleDateFormat("dd.MM.yyyy")
    private val currentDate: String = formatter.format(Date())

    private val todayTasksFlow = hideCompleted.flatMapLatest {
        todayTasksDao.getTodayTasks(currentDate, it ?: false)
    }
    val todayTasks = todayTasksFlow.asLiveData()
}
