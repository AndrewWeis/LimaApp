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
        d: MutableList<DataEntry>,
        t: String,
        n: String,
        a: Double,
        i: String,
        f: String,
        s: Boolean
    ) {
        val data = d
        val title = t
        val yearName = n
        val average = formatDouble(1, a)
        val date = i
        val format = f
        val isSoftMaximum = s
    }

    val chartDataList: MutableList<ChartData> = ArrayList()

    val month = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "June", "Jul", "Aug", "Sep", "Oct",
        "Nov", "Dec")

    private var _statYear: MutableLiveData<Boolean> = MutableLiveData()
    val statYear: LiveData<Boolean>
        get() = _statYear

    init {
        loadTasks()
    }

    private fun loadTasks() = viewModelScope.launch {
        loadAllTasks()
        loadCompletedTasks()
        loadProductivity()
        loadProductivityTendency()

        _statYear.value = true
    }

    private suspend fun loadAllTasks() {
        val calendar = Calendar.getInstance()
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

        val average = sum / 12

        yearList.forEach {
            data.add(ValueDataEntry(it.key, it.value))
        }

        chartDataList.add(ChartData(data, "All tasks", currentYearName, average.toDouble(),
            currentYearName, "{%value}", false))
    }

    private suspend fun loadCompletedTasks() {
        val calendar = Calendar.getInstance()
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

        val average = sum / 12

        yearList.forEach {
            data.add(ValueDataEntry(it.key, it.value))
        }

        chartDataList.add(ChartData(data, "Completed tasks", currentYearName, average.toDouble(),
            currentYearName, "{%value}", false))
    }

    private suspend fun loadProductivity() {
        val calendar = Calendar.getInstance()
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
                sum += 1.0
            } else {
                yearList[month[i - 1]] = yearListCompleted[month[i - 1]]!!.toDouble() /
                        yearListAll[month[i - 1]]!!.toDouble() * 100
                sum += yearList[month[i - 1]]!!
                nonEmptyCounter++
            }
        }

        val average = sum / 12

        yearList.forEach {
            data.add(ValueDataEntry(it.key, it.value))
        }

        chartDataList.add(ChartData(data, "Productivity", currentYearName, average,
            currentYearName, "{%value}%", true))
    }

    private suspend fun loadProductivityTendency() {
        val calendar = Calendar.getInstance()
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
            if (buf == 0.0) {
                buf = 100.0
            }
            if (yearListCompleted[month[i - 1]] == 0 || yearListAll[month[i - 1]] == 0) {
                yearList[month[i - 1]] = 0.0
                sum += 1.0
            } else {
                yearList[month[i - 1]] = (yearListCompleted[month[i - 1]]!!.toDouble() /
                        yearListAll[month[i - 1]]!!.toDouble() * 100) / buf * 100
                sum += yearList[month[i - 1]]!!
                nonEmptyCounter++
            }

            buf = yearList[month[i - 1]]!!
        }

        val average = sum / 12

        yearList.forEach {
            data.add(ValueDataEntry(it.key, it.value))
        }

        chartDataList.add(ChartData(data, "Productivity Tendency", currentYearName, average,
            currentYearName, "{%value}%", true))
    }

    private fun formatDouble(digits: Int, number: Double): Double {
        return BigDecimal(number).setScale(digits, RoundingMode.HALF_EVEN).toDouble()
    }
}
