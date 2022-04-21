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
import start.up.tracker.utils.TimeHelper
import java.lang.StringBuilder
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class AnalyticsWeekViewModel @Inject constructor(
    private val dao: AnalyticsDao,
) : ViewModel() {

    inner class ChartData(
        d: MutableList<DataEntry>,
        t: String,
        a: Double,
        i: String,
        f: String,
        s: Boolean,
    ) {
        val data = d
        val title = t
        val average = formatDouble(1, a)
        val date = i
        val format = f
        val isSoftMaximum = s
    }

    val chartDataList: MutableList<ChartData> = ArrayList()
    private val daysName = listOf("Mon", "Tue", "Wen", "Thu", "Fri", "Sat", "Sun")

    private var _statWeek: MutableLiveData<Boolean> = MutableLiveData(false)
    val statMonth: LiveData<Boolean>
        get() = _statWeek

    init {
        loadTasks()
    }

    private fun loadTasks() = viewModelScope.launch {
        loadAllTasks()
        loadCompletedTasks()
        loadProductivity()
        loadProductivityTendency()

        _statWeek.value = true
    }

    private suspend fun loadAllTasks() {
        val calendar = Calendar.getInstance()
        val currentYear: Int = calendar.get(Calendar.YEAR)
        val currentMonth: Int = calendar.get(Calendar.MONTH) + 1
        val currentWeek: Int = calendar.get(Calendar.WEEK_OF_YEAR) + 1
        val currentDay: Int = calendar.get(Calendar.DAY_OF_MONTH)
        val stats = dao.getStatWeek(currentYear, currentWeek)

        val data: MutableList<DataEntry> = ArrayList()
        val currentDate = getCurrentDate(calendar, currentYear)

        calendar.set(currentYear, currentMonth, currentDay)
        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_WEEK)

        val weekList: MutableMap<String, Int> = mutableMapOf()

        for (i in 0 until maxDay) {
            weekList[daysName[i]] = 0
        }

        var sum = 0

        stats.forEach {
            weekList[daysName[it.dayOfWeek - 1]] = it.allTasks
            sum += it.allTasks
        }

        val average = sum / maxDay

        weekList.forEach {
            data.add(ValueDataEntry(it.key, it.value))
        }

        chartDataList.add(ChartData(data, "All tasks", average.toDouble(), currentDate,
            "{%value}", false))
    }

    private suspend fun loadCompletedTasks() {
        val calendar = Calendar.getInstance()
        val currentYear: Int = calendar.get(Calendar.YEAR)
        val currentMonth: Int = calendar.get(Calendar.MONTH) + 1
        val currentWeek: Int = calendar.get(Calendar.WEEK_OF_YEAR) + 1
        val currentDay: Int = calendar.get(Calendar.DAY_OF_MONTH)
        val stats = dao.getStatWeek(currentYear, currentWeek)

        val data: MutableList<DataEntry> = ArrayList()
        val currentDate = getCurrentDate(calendar, currentYear)

        calendar.set(currentYear, currentMonth, currentDay)
        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_WEEK)

        val weekList: MutableMap<String, Int> = mutableMapOf()

        for (i in 0 until maxDay) {
            weekList[daysName[i]] = 0
        }

        var sum = 0

        stats.forEach {
            weekList[daysName[it.dayOfWeek - 1]] = it.completedTasks
            sum += it.completedTasks
        }

        val average = sum / maxDay

        weekList.forEach {
            data.add(ValueDataEntry(it.key, it.value))
        }

        chartDataList.add(ChartData(data, "Completed tasks", average.toDouble(), currentDate,
            "{%value}", false))
    }

    private suspend fun loadProductivity() {
        val calendar = Calendar.getInstance()
        val currentYear: Int = calendar.get(Calendar.YEAR)
        val currentMonth: Int = calendar.get(Calendar.MONTH) + 1
        val currentWeek: Int = calendar.get(Calendar.WEEK_OF_YEAR) + 1
        val currentDay: Int = calendar.get(Calendar.DAY_OF_MONTH)
        val stats = dao.getStatWeek(currentYear, currentWeek)

        val data: MutableList<DataEntry> = ArrayList()
        val currentDate = getCurrentDate(calendar, currentYear)

        calendar.set(currentYear, currentMonth, currentDay)
        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_WEEK)

        val weekList: MutableMap<String, Double> = mutableMapOf()

        for (i in 0 until maxDay) {
            weekList[daysName[i]] = 0.0
        }

        var sum = 0.0
        var nonEmptyCounter = 0

        stats.forEach {
            if (it.completedTasks == 0 || it.allTasks == 0) {
                weekList[daysName[it.dayOfWeek - 1]] = 0.0
                sum += 1.0
            } else {
                weekList[daysName[it.dayOfWeek - 1]] =
                    it.completedTasks.toDouble() / it.allTasks.toDouble() * 100
                sum += weekList[daysName[it.dayOfWeek - 1]]!!
                nonEmptyCounter++
            }
        }

        val average: Double = if (sum == 0.0 || nonEmptyCounter == 0) {
            0.0
        } else {
            sum / nonEmptyCounter
        }

        weekList.forEach {
            data.add(ValueDataEntry(it.key, it.value))
        }

        chartDataList.add(ChartData(data, "Productivity", average, currentDate,
            "{%value}%", true))
    }

    private suspend fun loadProductivityTendency() {
        val calendar = Calendar.getInstance()
        val currentYear: Int = calendar.get(Calendar.YEAR)
        val currentMonth: Int = calendar.get(Calendar.MONTH) + 1
        val currentWeek: Int = calendar.get(Calendar.WEEK_OF_YEAR) + 1
        val currentDay: Int = calendar.get(Calendar.DAY_OF_MONTH)
        val stats = dao.getStatWeek(currentYear, currentWeek)

        val data: MutableList<DataEntry> = ArrayList()
        val currentDate = getCurrentDate(calendar, currentYear)

        calendar.set(currentYear, currentMonth, currentDay)
        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_WEEK)

        val weekList: MutableMap<String, Double> = mutableMapOf()

        for (i in 0 until maxDay) {
            weekList[daysName[i]] = 0.0
        }

        var sum = 0.0
        var nonEmptyCounter = 0
        var buf = 0.0

        stats.forEach {
            if (buf == 0.0) {
                buf = 100.0
            }
            if (it.completedTasks == 0 || it.allTasks == 0) {
                weekList[daysName[it.dayOfWeek - 1]] = 0.0
                sum += 1.0
            } else {
                weekList[daysName[it.dayOfWeek - 1]] =
                    (it.completedTasks.toDouble() / it.allTasks.toDouble() * 100) / buf * 100
                sum += weekList[daysName[it.dayOfWeek - 1]]!!
                nonEmptyCounter++
            }

            buf = weekList[daysName[it.dayOfWeek - 1]]!!
        }

        val average: Double = if (sum == 0.0 || nonEmptyCounter == 0) {
            0.0
        } else {
            sum / nonEmptyCounter
        }

        weekList.forEach {
            data.add(ValueDataEntry(it.key, it.value))
        }

        chartDataList.add(ChartData(data, "Productivity Tendency", average, currentDate,
            "{%value}%", true))
    }

    private fun formatDouble(digits: Int, number: Double): Double {
        return BigDecimal(number).setScale(digits, RoundingMode.HALF_EVEN).toDouble()
    }

    private fun getCurrentDate(calendar: Calendar, currentYear: Int): String {
        return StringBuilder().append(TimeHelper.getStartOfWeekDayFromMillis(calendar.timeInMillis,
            (calendar.get(Calendar.DAY_OF_WEEK) - 1) % 7)).append(" ")
            .append(TimeHelper.getStartOfWeekMonthNameFromMillis(calendar.timeInMillis,
                (calendar.get(Calendar.DAY_OF_WEEK) - 1) % 7)).append(" - ")
            .append(TimeHelper.getEndOfWeekDayFromMillis(calendar.timeInMillis,
                (calendar.get(Calendar.DAY_OF_WEEK) - 1) % 7)).append(" ")
            .append(TimeHelper.getEndOfWeekMonthNameFromMillis(calendar.timeInMillis,
                (calendar.get(Calendar.DAY_OF_WEEK) - 1) % 7)).append(" ")
            .append(currentYear).toString()
    }
}
