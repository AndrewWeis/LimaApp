package start.up.tracker.ui.fragments.analytics

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.R
import start.up.tracker.databinding.FragmentAnalyticsMonthBinding
import start.up.tracker.databinding.FragmentAnalyticsWeekBinding
import start.up.tracker.mvvm.view_models.analytics.AnalyticsMonthViewModel
import start.up.tracker.mvvm.view_models.analytics.AnalyticsWeekViewModel
import java.util.*

@AndroidEntryPoint
class AnalyticsWeekFragment : Fragment(R.layout.fragment_analytics_week) {

    private val viewModel: AnalyticsWeekViewModel by viewModels()
    private var binding: FragmentAnalyticsWeekBinding? = null
    private var chartViews: MutableList<AnyChartView?> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAnalyticsWeekBinding.bind(view)

        initData()
        initObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun initData() {
        chartViews = mutableListOf(
            binding?.lineChartWeekAllTasks,
            binding?.lineChartWeekCompletedTasks)

        for (i in chartViews.indices) {
            chartViews[i]!!.setProgressBar(binding!!.progressBar)
        }
    }

    private fun initObservers() {
        viewModel.statMonth.observe(viewLifecycleOwner) {
            if (it == true) {
                initTasksChart()
            }
        }
    }

    private fun initTasksChart() {

        for (i in viewModel.chartDataList.indices) {
            APIlib.getInstance().setActiveAnyChartView(chartViews[i]);
            val chart = AnyChart.column()
            val column = chart.column(viewModel.chartDataList[i].data)

            column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(5.0)
                .offsetY(5.0)
                .format("Tasks: {%Value}{groupsSeparator: }");

            chart.xAxis(0).labels().fontSize(10)

            chart.yScale().minimumGap(1)
            chart.yAxis(0).labels().fontSize(10)

            chart.title(viewModel.chartDataList[i].title)
            chart.title().fontSize(12)
            chart.title().fontColor("#858585")

            chart.animation(true)

            chart.tooltip().positionMode(TooltipPositionMode.POINT)
            chart.interactivity().hoverMode(HoverMode.BY_X)

            chartViews[i]!!.setChart(chart)
        }

        /* data: MutableList<DataEntry> = ArrayList()
        data.add(ValueDataEntry("Mon", 7))
        data.add(ValueDataEntry("Tue", 10))
        data.add(ValueDataEntry("Wed", 6))
        data.add(ValueDataEntry("Thu", 9))
        data.add(ValueDataEntry("Fri", 7))
        data.add(ValueDataEntry("Sat", 4))
        data.add(ValueDataEntry("Sun", 2))

        val cartesian = AnyChart.column()
        val column = cartesian.column(data)

        column.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(5.0)
            .offsetY(5.0)
            .format("Tasks: {%Value}{groupsSeparator: }");


        cartesian.title("Completed tasks")
        cartesian.title().fontSize(12)
        cartesian.title().fontColor("#858585")


        cartesian.animation(true)
        cartesian.yScale().maximumGap(1)


        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)

        binding!!.lineChartWeekAllTasks.setChart(cartesian)*/
    }
}