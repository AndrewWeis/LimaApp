package start.up.tracker.mvvm.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import start.up.tracker.data.database.dao.TaskDao
import start.up.tracker.data.entities.DayStat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.forEach
import kotlin.collections.mutableMapOf
import kotlin.collections.set

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


        data.add(ValueDataEntry("Jan", yearList[1]))
        data.add(ValueDataEntry("Feb", yearList[2]))
        data.add(ValueDataEntry("Mar", yearList[3]))
        data.add(ValueDataEntry("Apr", yearList[4]))
        data.add(ValueDataEntry("May", yearList[5]))
        data.add(ValueDataEntry("Jun", yearList[6]))
        data.add(ValueDataEntry("Jul", yearList[7]))
        data.add(ValueDataEntry("Aug", yearList[8]))
        data.add(ValueDataEntry("Sep", yearList[9]))
        data.add(ValueDataEntry("Oct", yearList[10]))
        data.add(ValueDataEntry("Nov", yearList[11]))
        data.add(ValueDataEntry("Dec", yearList[12]))

        _statYear.value = true
    }

}