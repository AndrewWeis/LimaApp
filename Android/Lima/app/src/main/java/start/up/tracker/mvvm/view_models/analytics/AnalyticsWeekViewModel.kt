package start.up.tracker.mvvm.view_models.analytics

import androidx.lifecycle.*
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import start.up.tracker.database.dao.AnalyticsDao
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.utils.TimeHelper
import java.lang.StringBuilder
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@HiltViewModel
class AnalyticsWeekViewModel @Inject constructor(
    private val analyticsDao: AnalyticsDao,
    private val taskDao: TaskDao,
) : ViewModel() {

    inner class ChartData(
        da: MutableList<DataEntry>,
        ti: String,
        av: String,
        to: String,
        de: String,
        fo: String,
        smax: Boolean,
        smin: Boolean,
        sh: Int,
        dn: String,
    ) {
        var data = da
        val title = ti
        var average = av
        var total = to
        var date = de
        val format = fo
        val isSoftMaximum = smax
        val isSoftMinimum = smin
        var shift = sh
        val description = dn
    }

    fun update(id: Int, sh: Int) = viewModelScope.launch {
        when (chartDataList[id].title) {
            "All tasks" -> {
                chartDataList[id] = loadAllTasks(sh + chartDataList[id].shift)
            }
            "Completed tasks" -> {
                chartDataList[id] = loadCompletedTasks(sh + chartDataList[id].shift)
            }
            "Productivity" -> {
                chartDataList[id] = loadProductivity(sh + chartDataList[id].shift)
            }
            "Productivity Tendency" ->
                chartDataList[id] = loadProductivityTendency(sh + chartDataList[id].shift)
        }

        _statWeek2.value = true
    }

    val chartDataList: MutableList<ChartData> = ArrayList()
    private val daysName = listOf("Mon", "Tue", "Wen", "Thu", "Fri", "Sat", "Sun")

    private var _statWeek: MutableLiveData<Boolean> = MutableLiveData(false)
    val statWeek: LiveData<Boolean>
        get() = _statWeek

    private var _statWeek2: MutableLiveData<Boolean> = MutableLiveData(false)
    val statWeek2: LiveData<Boolean>
        get() = _statWeek2

    private var _statWeek3: MutableLiveData<Boolean> = MutableLiveData(false)
    val statWeek3: LiveData<Boolean>
        get() = _statWeek3

    init {
        loadTasks()
    }

    private fun loadTasks() = viewModelScope.launch {
        chartDataList.add(loadAllTasks(0))
        chartDataList.add(loadCompletedTasks(0))
        chartDataList.add(loadProductivity(0))
        chartDataList.add(loadProductivityTendency(0))

        _statWeek.value = true
    }

    /*private fun getHabitStats(calendar1: Calendar) = viewModelScope.launch {
        val allHabits = taskDao.getAllHabits()

        for (i in 0 until 7) {
            habitStats[i] = 0
        }

        for (habit in allHabits) {
            val calendar2 = Calendar.getInstance()
            val currentYear: Int = calendar2.get(Calendar.YEAR)
            val currentMonth: Int = calendar2.get(Calendar.MONTH) + 1
            var currentWeek: Int = calendar2.get(Calendar.WEEK_OF_YEAR) + 1
            val currentDay: Int = calendar2.get(Calendar.DAY_OF_MONTH)
            calendar2.timeInMillis = habit.date!!
            calendar1.timeInMillis = calendar1.timeInMillis +
                    86400000 * (7 - (calendar1.get(Calendar.DAY_OF_WEEK) - 1) % 7)
            while (calendar2.timeInMillis < calendar1.timeInMillis) {
                if (calendar2.get(Calendar.WEEK_OF_YEAR) == calendar1.get(Calendar.WEEK_OF_YEAR)) {
                    habitStats[(Calendar.DAY_OF_WEEK - 1 + 7) % 7] =
                        habitStats[(Calendar.DAY_OF_WEEK - 1 + 7) % 7]!! + 1
                }
                calendar2.timeInMillis += habit.shift
            }
        }

        _statWeek3.value = true
    }*/

    private suspend fun loadAllTasks(shift: Int): ChartData {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + shift * 7)
        val currentYear: Int = calendar.get(Calendar.YEAR)
        val currentMonth: Int = calendar.get(Calendar.MONTH) + 1
        var currentWeek: Int = calendar.get(Calendar.WEEK_OF_YEAR) + 1
        val currentDay: Int = calendar.get(Calendar.DAY_OF_MONTH)

        /////////////////////////////

        val habitStats: HashMap<Int, Int> = HashMap()
        val allHabits = taskDao.getAllHabits()

        for (i in 0 until 7) {
            habitStats[i] = 0
        }

        val calendar1 = Calendar.getInstance();

        for (habit in allHabits) {
            val habitStatsLocal: HashMap<Int, Int> = HashMap()
            for (i in 0 until 7) {
                habitStatsLocal[i] = 0
            }

            val calendar2 = Calendar.getInstance()
            calendar2.timeInMillis = habit.date!!
            var year = calendar2.get(Calendar.YEAR)
            calendar1.timeInMillis = calendar.timeInMillis +
                    86400000 * (7 - (calendar.get(Calendar.DAY_OF_WEEK) - 1) % 7)
            while (calendar2.timeInMillis < calendar1.timeInMillis) {
                if (year != calendar2.get(Calendar.YEAR) &&
                    calendar2.get(Calendar.DAY_OF_WEEK) == 0) {
                    for (i in 0 until 7) {
                        habitStatsLocal[i] = 0
                    }
                    year = calendar2.get(Calendar.YEAR)
                }
                if (calendar2.get(Calendar.WEEK_OF_YEAR) == calendar1.get(Calendar.WEEK_OF_YEAR)) {
                    habitStatsLocal[(calendar2.get(Calendar.DAY_OF_WEEK) - 2 + 7) % 7] =
                        habitStatsLocal[(calendar2.get(Calendar.DAY_OF_WEEK) - 2 + 7) % 7]!! + 1
                }
                calendar2.timeInMillis += habit.shift
            }

            for (i in 0 until 7) {
                habitStats[i] = habitStats[i]!! + habitStatsLocal[i]!!
            }
        }

        /////////////////////////////
        val stats = analyticsDao.getStatWeek(currentYear, currentWeek)

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
            weekList[daysName[(it.dayOfWeek - 1 + 7) % 7]] = it.allTasks
        }

        for (i in 0 until 7) {
            weekList[daysName[i]] = weekList[daysName[i]]!! + habitStats[i]!!
            sum += weekList[daysName[i]]!!
        }

        val average = sum.toDouble() / 7

        weekList.forEach {
            data.add(ValueDataEntry(it.key, it.value))
        }

        return (ChartData(data, "All tasks", formatDouble(average), formatDouble(sum.toDouble()),
            currentDate, "{%value}", false, false, shift,
            "Number of all your tasks in day during the week"
        ))
    }

    private suspend fun loadCompletedTasks(shift: Int): ChartData {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + shift * 7)
        val currentYear: Int = calendar.get(Calendar.YEAR)
        val currentMonth: Int = calendar.get(Calendar.MONTH) + 1
        var currentWeek: Int = calendar.get(Calendar.WEEK_OF_YEAR) + 1
        val currentDay: Int = calendar.get(Calendar.DAY_OF_MONTH)
        val stats = analyticsDao.getStatWeek(currentYear, currentWeek)

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
            weekList[daysName[(it.dayOfWeek - 1 + 7) % 7]] = it.completedTasks
            sum += it.completedTasks
        }

        val average = sum.toDouble() / 7

        weekList.forEach {
            data.add(ValueDataEntry(it.key, it.value))
        }

        return (ChartData(data, "Completed tasks", formatDouble(average),
            formatDouble(sum.toDouble()), currentDate, "{%value}", false, false,
            shift, "Number of your completed tasks in day during the week"
        ))
    }

    private suspend fun loadProductivity(shift: Int): ChartData {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + shift * 7)
        val currentYear: Int = calendar.get(Calendar.YEAR)
        val currentMonth: Int = calendar.get(Calendar.MONTH) + 1
        var currentWeek: Int = calendar.get(Calendar.WEEK_OF_YEAR) + 1
        val currentDay: Int = calendar.get(Calendar.DAY_OF_MONTH)
        val stats = analyticsDao.getStatWeek(currentYear, currentWeek)

        /////////////////////////////

        val habitStats: HashMap<Int, Int> = HashMap()
        val allHabits = taskDao.getAllHabits()

        for (i in 0 until 7) {
            habitStats[i] = 0
        }

        val calendar1 = Calendar.getInstance();

        for (habit in allHabits) {
            val habitStatsLocal: HashMap<Int, Int> = HashMap()
            for (i in 0 until 7) {
                habitStatsLocal[i] = 0
            }

            val calendar2 = Calendar.getInstance()
            calendar2.timeInMillis = habit.date!!
            var year = calendar2.get(Calendar.YEAR)
            calendar1.timeInMillis = calendar.timeInMillis +
                    86400000 * (7 - (calendar.get(Calendar.DAY_OF_WEEK) - 1) % 7)
            while (calendar2.timeInMillis < calendar1.timeInMillis) {
                if (year != calendar2.get(Calendar.YEAR) &&
                    calendar2.get(Calendar.DAY_OF_WEEK) == 0) {
                    for (i in 0 until 7) {
                        habitStatsLocal[i] = 0
                    }
                    year = calendar2.get(Calendar.YEAR)
                }
                if (calendar2.get(Calendar.WEEK_OF_YEAR) == calendar1.get(Calendar.WEEK_OF_YEAR)) {
                    habitStatsLocal[(calendar2.get(Calendar.DAY_OF_WEEK) - 2 + 7) % 7] =
                        habitStatsLocal[(calendar2.get(Calendar.DAY_OF_WEEK) - 2 + 7) % 7]!! + 1
                }
                calendar2.timeInMillis += habit.shift
            }

            for (i in 0 until 7) {
                habitStats[i] = habitStats[i]!! + habitStatsLocal[i]!!
            }
        }

        /////////////////////////////

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
            if (it.completedTasks == 0 ||
                (habitStats[(it.dayOfWeek - 1 + 7) % 7]!! + it.allTasks) == 0) {
                weekList[daysName[(it.dayOfWeek - 1 + 7) % 7]] = 0.0
            } else {
                weekList[daysName[(it.dayOfWeek - 1 + 7) % 7]] =
                    it.completedTasks.toDouble() / (habitStats[(it.dayOfWeek - 1 + 7) % 7]!!
                            + it.allTasks) * 100
                sum += weekList[daysName[(it.dayOfWeek - 1 + 7) % 7]]!!
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

        return (ChartData(data, "Productivity", formatDouble(average) + "%",
            formatDouble(-1.0), currentDate, "{%value}%", true, false, shift,
            "The ratio of all tasks you completed in day during the week to all created tasks"
        ))
    }

    private suspend fun loadProductivityTendency(shift: Int): ChartData {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + shift * 7)
        val currentYear: Int = calendar.get(Calendar.YEAR)
        val currentMonth: Int = calendar.get(Calendar.MONTH) + 1
        var currentWeek: Int = calendar.get(Calendar.WEEK_OF_YEAR) + 1
        val currentDay: Int = calendar.get(Calendar.DAY_OF_MONTH)
        val stats = analyticsDao.getStatWeek(currentYear, currentWeek)

        /////////////////////////////

        val habitStats: HashMap<Int, Int> = HashMap()
        val allHabits = taskDao.getAllHabits()

        for (i in 0 until 7) {
            habitStats[i] = 0
        }

        val calendar1 = Calendar.getInstance();

        for (habit in allHabits) {
            val habitStatsLocal: HashMap<Int, Int> = HashMap()
            for (i in 0 until 7) {
                habitStatsLocal[i] = 0
            }

            val calendar2 = Calendar.getInstance()
            calendar2.timeInMillis = habit.date!!
            var year = calendar2.get(Calendar.YEAR)
            calendar1.timeInMillis = calendar.timeInMillis +
                    86400000 * (7 - (calendar.get(Calendar.DAY_OF_WEEK) - 1) % 7)
            while (calendar2.timeInMillis < calendar1.timeInMillis) {
                if (year != calendar2.get(Calendar.YEAR) &&
                    calendar2.get(Calendar.DAY_OF_WEEK) == 0) {
                    for (i in 0 until 7) {
                        habitStatsLocal[i] = 0
                    }
                    year = calendar2.get(Calendar.YEAR)
                }
                if (calendar2.get(Calendar.WEEK_OF_YEAR) == calendar1.get(Calendar.WEEK_OF_YEAR)) {
                    habitStatsLocal[(calendar2.get(Calendar.DAY_OF_WEEK) - 2 + 7) % 7] =
                        habitStatsLocal[(calendar2.get(Calendar.DAY_OF_WEEK) - 2 + 7) % 7]!! + 1
                }
                calendar2.timeInMillis += habit.shift
            }

            for (i in 0 until 7) {
                habitStats[i] = habitStats[i]!! + habitStatsLocal[i]!!
            }
        }

        /////////////////////////////

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
            if (it.completedTasks == 0 ||
                (habitStats[(it.dayOfWeek - 1 + 7) % 7]!! + it.allTasks) == 0) {
                weekList[daysName[(it.dayOfWeek - 1 + 7) % 7]] = 0.0
                buf = 0.0
            } else {
                val new = it.completedTasks.toDouble() /
                        (habitStats[(it.dayOfWeek - 1 + 7) % 7]!! + it.allTasks) * 100
                if (new == 0.0) {
                    weekList[daysName[(it.dayOfWeek - 1 + 7) % 7]] = 0.0
                    buf = 0.0
                } else {
                    weekList[daysName[(it.dayOfWeek - 1 + 7) % 7]] = (new - buf) / new * 100
                    buf = new
                }
                sum += weekList[daysName[(it.dayOfWeek - 1 + 7) % 7]]!!
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

        return (ChartData(data, "Productivity Tendency", formatDouble(average) + "%",
            formatDouble(-1.0), currentDate, "{%value}%", true, true, shift,
            "The ratio of your productivity compared to the previous day of the week"
        ))
    }

    private fun formatDouble(number: Double): String {
        val str = BigDecimal(number).setScale(1, RoundingMode.HALF_EVEN).toString()
        val lastSymbol = str.substring(str.length - 1)
        return if (lastSymbol == "0") {
            str.substring(0, str.length - 2);
        } else {
            str
        }
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
