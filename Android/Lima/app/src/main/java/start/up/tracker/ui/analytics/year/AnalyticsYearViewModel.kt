package start.up.tracker.ui.analytics.year

import android.util.Log
import androidx.lifecycle.*
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import start.up.tracker.data.db.TaskDao
import start.up.tracker.data.models.DayStat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

@HiltViewModel
class AnalyticsYearViewModel @Inject constructor(
    private val taskDao: TaskDao
) :ViewModel() {

    val data: MutableList<DataEntry> = ArrayList()

    private var _statYear: MutableLiveData<Boolean> = MutableLiveData()
    val statYear: LiveData<Boolean>
        get() = _statYear

    val currentYear: String = SimpleDateFormat("yyyy").format(Date())

    init {
        loadStatYear()
    }

    private fun loadStatYear() = viewModelScope.launch {
        val stats = taskDao.getStatYear(currentYear.toInt())

        initYearData(stats)
    }

    private fun initYearData(stats: List<DayStat>) {

        val yearList: MutableMap<Int, Int> = mutableMapOf(
            1 to 0, 2 to 0, 3 to 0, 4 to 0,
            5 to 0, 6 to 0, 7 to 0, 8 to 0,
            9 to 0, 10 to 0, 11 to 0, 12 to 0,
        )

        stats.forEach {
            val currentValue = yearList[it.month]
            yearList[it.month] = currentValue!! + it.completedTasks
        }


        data.add(ValueDataEntry("Ja", yearList[1]))
        data.add(ValueDataEntry("Fe", yearList[2]))
        data.add(ValueDataEntry("Ma", yearList[3]))
        data.add(ValueDataEntry("Ap", yearList[4]))
        data.add(ValueDataEntry("Ma", yearList[5]))
        data.add(ValueDataEntry("Jn", yearList[6]))
        data.add(ValueDataEntry("Jl", yearList[7]))
        data.add(ValueDataEntry("Au", yearList[8]))
        data.add(ValueDataEntry("Se", yearList[9]))
        data.add(ValueDataEntry("Oc", yearList[10]))
        data.add(ValueDataEntry("No", yearList[11]))
        data.add(ValueDataEntry("De", yearList[12]))

        _statYear.value = true
    }

}