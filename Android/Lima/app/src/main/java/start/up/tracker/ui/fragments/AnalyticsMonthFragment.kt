package start.up.tracker.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
    private lateinit var binding: FragmentAnalyticsMonthBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAnalyticsMonthBinding.bind(view)

        binding.lineChartMonth.setProgressBar(binding.progressBar)

        viewModel.statMonth.observe(viewLifecycleOwner) {
            if (it == true) {
                initTasksChart()
            }
        }
    }

    private fun initTasksChart() {

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

        binding.lineChartMonth.setChart(chart)
    }
}