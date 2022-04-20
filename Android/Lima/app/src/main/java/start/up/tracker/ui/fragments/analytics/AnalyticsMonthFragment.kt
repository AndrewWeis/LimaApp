package start.up.tracker.ui.fragments.analytics

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.anychart.APIlib
import com.anychart.AnyChart
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
        binding?.lineChartMonthAllTasks?.setProgressBar(binding!!.progressBar)
        binding?.lineChartMonthCompletedTasks?.setProgressBar(binding!!.progressBar)
        binding?.lineChartMonthUncompletedTasks?.setProgressBar(binding!!.progressBar)
    }

    private fun initObservers() {
        viewModel.statMonth.observe(viewLifecycleOwner) {
            if (it == true) {
                initTasksChart()
            }
        }
    }

    private fun initTasksChart() {
        APIlib.getInstance().setActiveAnyChartView(binding?.lineChartMonthAllTasks);
        val chart = AnyChart.column()
        val column = chart.column(viewModel.data)

        column.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(5.0)
            .offsetY(5.0)
            .format("Tasks: {%Value}{groupsSeparator: }");

        chart.xAxis(0).labels().fontSize(10)
        chart.xAxis(0).title(viewModel.currentMonthName)

        chart.yScale().minimumGap(1)
        chart.yAxis(0).labels().fontSize(10)

        chart.title("Completed tasks")
        chart.title().fontSize(12)
        chart.title().fontColor("#858585")

        chart.animation(true)

        chart.tooltip().positionMode(TooltipPositionMode.POINT)
        chart.interactivity().hoverMode(HoverMode.BY_X)

        binding?.lineChartMonthAllTasks?.setChart(chart)

        APIlib.getInstance().setActiveAnyChartView(binding?.lineChartMonthCompletedTasks);
        val chart2 = AnyChart.column()
        val column2 = chart2.column(viewModel.data)

        column2.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(5.0)
            .offsetY(5.0)
            .format("Tasks: {%Value}{groupsSeparator: }");

        chart2.xAxis(0).labels().fontSize(10)
        chart2.xAxis(0).title(viewModel.currentMonthName)

        chart2.yScale().minimumGap(1)
        chart2.yAxis(0).labels().fontSize(10)

        chart2.title("Completed tasks")
        chart2.title().fontSize(12)
        chart2.title().fontColor("#858585")

        chart2.animation(true)

        chart2.tooltip().positionMode(TooltipPositionMode.POINT)
        chart2.interactivity().hoverMode(HoverMode.BY_X)

        binding?.lineChartMonthCompletedTasks?.setChart(chart2)

        /////////////////

        APIlib.getInstance().setActiveAnyChartView(binding?.lineChartMonthUncompletedTasks);
        val chart3 = AnyChart.column()
        val column3 = chart3.column(viewModel.data)

        column3.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(5.0)
            .offsetY(5.0)
            .format("Tasks: {%Value}{groupsSeparator: }");

        chart3.xAxis(0).labels().fontSize(10)
        chart3.xAxis(0).title(viewModel.currentMonthName)

        chart3.yScale().minimumGap(1)
        chart3.yAxis(0).labels().fontSize(10)

        chart3.title("Completed tasks")
        chart3.title().fontSize(12)
        chart3.title().fontColor("#858585")

        chart3.animation(true)

        chart3.tooltip().positionMode(TooltipPositionMode.POINT)
        chart3.interactivity().hoverMode(HoverMode.BY_X)

        binding?.lineChartMonthUncompletedTasks?.setChart(chart3)
    }
}
