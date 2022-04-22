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
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.set

@HiltViewModel
class AnalyticsYearViewModel @Inject constructor(
    private val dao: AnalyticsDao,
) : ViewModel() {

    inner class ChartData(
        da: MutableList<DataEntry>,
        ti: String,
        av: String,
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

        _statYear2.value = true
    }

    val chartDataList: MutableList<ChartData> = ArrayList()

    val month = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "June", "Jul", "Aug", "Sep", "Oct",
        "Nov", "Dec")

    private var _statYear: MutableLiveData<Boolean> = MutableLiveData()
    val statYear: LiveData<Boolean>
        get() = _statYear

    private var _statYear2: MutableLiveData<Boolean> = MutableLiveData()
    val statYear2: LiveData<Boolean>
        get() = _statYear2

    init {
        loadTasks()
    }

    private fun loadTasks() = viewModelScope.launch {
        chartDataList.add(loadAllTasks(0))
        chartDataList.add(loadCompletedTasks(0))
        chartDataList.add(loadProductivity(0))
        chartDataList.add(loadProductivityTendency(0))

        _statYear.value = true
    }

    private suspend fun loadAllTasks(shift: Int): ChartData {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + shift)
        val currentYear: Int = calendar.get(Calendar.YEAR)
        val stats = dao.getStatYear(currentYear)

        val data: MutableList<DataEntry> = ArrayList()
        val currentYearName = SimpleDateFormat("yyyy").format(calendar.time)

        val yearList: MutableMap<String, Int> = mutableMapOf()

        for (i in 0 until 12) {
            yearList[month[i]] = 0
        }

        var sum = 0

        stats.forEach {
            val currentValue = yearList[month[it.month - 1]]
            yearList[month[it.month - 1]] = currentValue!! + it.allTasks
            sum += yearList[month[it.month - 1]]!!
        }

        val average = sum.toDouble() / 12

        yearList.forEach {
            data.add(ValueDataEntry(it.key, it.value))
        }

        return ChartData(data, "All tasks", formatDouble(1, average).toString(),
            currentYearName, "{%value}", false, false, shift,
            "Number of all your tasks in months of the year"
        )
    }

    private suspend fun loadCompletedTasks(shift: Int): ChartData {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + shift)
        val currentYear: Int = calendar.get(Calendar.YEAR)
        val stats = dao.getStatYear(currentYear)

        val data: MutableList<DataEntry> = ArrayList()
        val currentYearName = SimpleDateFormat("yyyy").format(calendar.time)

        val yearList: MutableMap<String, Int> = mutableMapOf()

        for (i in 0 until 12) {
            yearList[month[i]] = 0
        }

        var sum = 0

        stats.forEach {
            val currentValue = yearList[month[it.month - 1]]
            yearList[month[it.month - 1]] = currentValue!! + it.completedTasks
            sum += yearList[month[it.month - 1]]!!
        }

        val average = sum.toDouble() / 12

        yearList.forEach {
            data.add(ValueDataEntry(it.key, it.value))
        }

        return (ChartData(data, "Completed tasks", formatDouble(1, average).toString(),
            currentYearName, "{%value}", false, false, shift,
            "Number of your completed tasks in months of the year"
        ))
    }

    private suspend fun loadProductivity(shift: Int): ChartData {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + shift)
        val currentYear: Int = calendar.get(Calendar.YEAR)
        val stats = dao.getStatYear(currentYear)

        val data: MutableList<DataEntry> = ArrayList()
        val currentYearName = SimpleDateFormat("yyyy").format(calendar.time)

        val yearList: MutableMap<String, Double> = mutableMapOf()
        val yearListAll: MutableMap<String, Int> = mutableMapOf()
        val yearListCompleted: MutableMap<String, Int> = mutableMapOf()

        for (i in 0 until 12) {
            yearList[month[i]] = 0.0
            yearListAll[month[i]] = 0
            yearListCompleted[month[i]] = 0
        }

        stats.forEach {
            val currentValueAll = yearListAll[month[it.month - 1]]
            yearListAll[month[it.month - 1]] = currentValueAll!! + it.allTasks

            val currentValueCompleted = yearListCompleted[month[it.month - 1]]
            yearListCompleted[month[it.month - 1]] = currentValueCompleted!! + it.completedTasks
        }

        var sum = 0.0
        var nonEmptyCounter = 0

        for (i in 1..12) {
            if (yearListCompleted[month[i - 1]] == 0 || yearListAll[month[i - 1]] == 0) {
                yearList[month[i - 1]] = 0.0
            } else {
                yearList[month[i - 1]] = yearListCompleted[month[i - 1]]!!.toDouble() /
                        yearListAll[month[i - 1]]!!.toDouble() * 100
                sum += yearList[month[i - 1]]!!
                nonEmptyCounter++
            }
        }

        val average: Double = if (sum == 0.0 || nonEmptyCounter == 0) {
            0.0
        } else {
            sum / nonEmptyCounter
        }

        yearList.forEach {
            data.add(ValueDataEntry(it.key, it.value))
        }

        return (ChartData(data, "Productivity", formatDouble(1, average).toString() + "%",
            currentYearName, "{%value}%", true, false, shift,
            "The ratio of all tasks you completed in months of the year to all created tasks"
        ))
    }

    private suspend fun loadProductivityTendency(shift: Int): ChartData {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + shift)
        val currentYear: Int = calendar.get(Calendar.YEAR)
        val stats = dao.getStatYear(currentYear)

        val data: MutableList<DataEntry> = ArrayList()
        val currentYearName = SimpleDateFormat("yyyy").format(calendar.time)

        val yearList: MutableMap<String, Double> = mutableMapOf()
        val yearListAll: MutableMap<String, Int> = mutableMapOf()
        val yearListCompleted: MutableMap<String, Int> = mutableMapOf()

        for (i in 0 until 12) {
            yearList[month[i]] = 0.0
            yearListAll[month[i]] = 0
            yearListCompleted[month[i]] = 0
        }

        stats.forEach {
            val currentValueAll = yearListAll[month[it.month - 1]]
            yearListAll[month[it.month - 1]] = currentValueAll!! + it.allTasks

            val currentValueCompleted = yearListCompleted[month[it.month - 1]]
            yearListCompleted[month[it.month - 1]] = currentValueCompleted!! + it.completedTasks
        }

        var sum = 0.0
        var nonEmptyCounter = 0
        var buf = 0.0

        for (i in 1..12) {
            if (yearListCompleted[month[i - 1]] == 0 || yearListAll[month[i - 1]] == 0) {
                yearList[month[i - 1]] = 0.0
                buf = 0.0
            } else {
                val new = yearListCompleted[month[i - 1]]!!.toDouble() /
                        yearListAll[month[i - 1]]!!.toDouble() * 100
                if (new == 0.0) {
                    yearList[month[i - 1]] = 0.0
                    buf = 0.0
                } else {
                    yearList[month[i - 1]] = (new - buf) / new * 100
                    buf = new
                }
                sum += yearList[month[i - 1]]!!
                nonEmptyCounter++
            }
        }

        val average: Double = if (sum == 0.0 || nonEmptyCounter == 0) {
            0.0
        } else {
            sum / nonEmptyCounter
        }

        yearList.forEach {
            data.add(ValueDataEntry(it.key, it.value))
        }

        return (ChartData(data, "Productivity Tendency",
            formatDouble(1, average).toString() + "%",
            currentYearName, "{%value}%", true, true, shift,
            "The ratio of your productivity compared to the previous month of the year"
        ))
    }

    private fun formatDouble(digits: Int, number: Double): Double {
        return BigDecimal(number).setScale(digits, RoundingMode.HALF_EVEN).toDouble()
    }
}
