package start.up.tracker.ui.fragments.analytics

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.R
import start.up.tracker.databinding.FragmentAnalyticsMonthBinding
import start.up.tracker.mvvm.view_models.analytics.AnalyticsMonthViewModel

@AndroidEntryPoint
class AnalyticsMonthFragment : Fragment(R.layout.fragment_analytics_month) {

    private val viewModel: AnalyticsMonthViewModel by viewModels()
    private var binding: FragmentAnalyticsMonthBinding? = null
    private var chartViews: MutableList<AnyChartView?> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAnalyticsMonthBinding.bind(view)

        initData()
        initObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun initData() {
        chartViews = mutableListOf(
            binding!!.lineChartMonthAllTasks,
            binding!!.lineChartMonthCompletedTasks,
            binding!!.lineChartMonthProductivity)

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
            APIlib.getInstance().setActiveAnyChartView(chartViews[i])

            binding!!.monthDate.text = viewModel.chartDataList[i].date
            binding!!.monthAverage.text = viewModel.chartDataList[i].average.toString()

            val chart = AnyChart.column()
            val column = chart.column(viewModel.chartDataList[i].data)

            column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(5.0)
                .offsetY(5.0)
                .format(viewModel.chartDataList[i].title + ": {%Value}{groupsSeparator: }");

            chart.xAxis(0).labels().fontSize(10)
            chart.xAxis(0).title(viewModel.chartDataList[i].monthName)

            chart.yScale().minimumGap(1)
            chart.yScale().ticks().allowFractional(false);
            chart.yAxis(0).labels().fontSize(10)
            chart.yAxis(0).labels().format(viewModel.chartDataList[i].format)

            chart.title(viewModel.chartDataList[i].title)
            chart.title().fontSize(12)
            chart.title().fontColor("#858585")

            chart.animation(true)

            chart.tooltip().positionMode(TooltipPositionMode.POINT)
            chart.interactivity().hoverMode(HoverMode.BY_X)

            chartViews[i]!!.setChart(chart)
        }

    }
}
