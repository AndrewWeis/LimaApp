package start.up.tracker.ui.today

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import start.up.tracker.data.db.TaskDao
import start.up.tracker.data.models.Task
import start.up.tracker.data.models.TodayTask
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val taskDao: TaskDao,
) : ViewModel() {

    private val formatter = SimpleDateFormat("dd.MM.yyyy")
    private val currentDate: String = formatter.format(Date())

    val todayTasks: LiveData<List<TodayTask>> = taskDao.getTodayTasks(currentDate).asLiveData()
}