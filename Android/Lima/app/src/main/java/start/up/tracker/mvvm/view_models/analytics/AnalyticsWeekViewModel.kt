package start.up.tracker.mvvm.view_models.analytics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import start.up.tracker.database.dao.AnalyticsDao
import start.up.tracker.entities.DayStat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class AnalyticsWeekViewModel @Inject constructor(
    private val dao: AnalyticsDao
) : ViewModel() {

    inner class ChartData(d: MutableList<DataEntry>, t: String) {
        val data = d
        val title = t
    }

    val chartDataList : MutableList<ChartData> = ArrayList()
    private val daysName = listOf("Mon", "Tue", "Wen", "Thu", "Fri", "Sat", "Sun")

    private var _statWeek: MutableLiveData<Boolean> = MutableLiveData(false)
    val statMonth: LiveData<Boolean>
        get() = _statWeek

    private val calendar = Calendar.getInstance()
    private val currentYear: Int = calendar.get(Calendar.YEAR)
    private val currentMonth: Int = calendar.get(Calendar.MONTH) + 1
    private val currentWeek: Int = calendar.get(Calendar.WEEK_OF_YEAR) + 1
    private val currentDay: Int = calendar.get(Calendar.DAY_OF_MONTH)

    init {
        loadTasks()
    }

    private fun loadTasks() = viewModelScope.launch {
        val stats = dao.getStatWeek(currentYear, currentWeek)

        loadCompletedTasks(stats)
        loadAllTasks(stats)

        _statWeek.value = true
    }

    private fun loadCompletedTasks(stats: List<DayStat>) {
        val data: MutableList<DataEntry> = ArrayList()

        calendar.set(currentYear, currentMonth, currentDay)
        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_WEEK)

        val weekList: MutableMap<String, Int> = mutableMapOf()

        for (i in 0 until maxDay) {
            weekList[daysName[i]] = 0
        }

        stats.forEach {
            weekList[daysName[it.dayOfWeek - 1]] = it.completedTasks
        }

        weekList.forEach {
            data.add(ValueDataEntry(it.key, it.value))
        }

        chartDataList.add(ChartData(data, "Completed tasks", ))
    }

    private fun loadAllTasks(stats: List<DayStat>) {
        val data: MutableList<DataEntry> = ArrayList()

        calendar.set(currentYear, currentMonth, currentDay)
        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_WEEK)

        val weekList: MutableMap<String, Int> = mutableMapOf()

        for (i in 0 until maxDay) {
            weekList[daysName[i]] = 0
        }

        stats.forEach {
            weekList[daysName[it.dayOfWeek - 1]] = it.allTasks
        }

        weekList.forEach {
            data.add(ValueDataEntry(it.key, it.value))
        }

        chartDataList.add(ChartData(data, "All tasks", ))
    }
}
