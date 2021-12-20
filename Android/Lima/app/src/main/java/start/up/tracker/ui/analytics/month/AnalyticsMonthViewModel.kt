package start.up.tracker.ui.analytics.month

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import start.up.tracker.data.db.TaskDao
import start.up.tracker.data.models.DayStat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class AnalyticsMonthViewModel @Inject constructor(
    private val taskDao: TaskDao
) : ViewModel() {

    val data: MutableList<DataEntry> = ArrayList()

    private var _statMonth: MutableLiveData<Boolean> = MutableLiveData(false)
    val statMonth: LiveData<Boolean>
        get() = _statMonth

    private val calendar = Calendar.getInstance()
    private val currentYear: Int = calendar.get(Calendar.YEAR)
    private val currentMonth: Int = calendar.get(Calendar.MONTH) + 1
    val currentMonthName: String = SimpleDateFormat("MMMM").format(calendar.time)

    init {
        loadStatMonth()
    }

    private fun loadStatMonth() = viewModelScope.launch {
        val stats = taskDao.getStatMonth(currentYear, currentMonth)
        initMonthData(stats)
    }

    private fun initMonthData(stats: List<DayStat>) {

        calendar.set(currentYear, currentMonth, 1)
        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val monthList: MutableMap<Int, Int> = mutableMapOf()

        for (i in 1..maxDay) {
            monthList[i] = 0
        }

        stats.forEach {
            monthList[it.day] = it.completedTasks
        }

        monthList.forEach {
            data.add(ValueDataEntry(it.key.toString(), it.value))
        }

        _statMonth.value = true
    }

}