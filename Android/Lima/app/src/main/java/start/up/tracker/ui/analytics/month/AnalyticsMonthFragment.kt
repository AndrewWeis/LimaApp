package start.up.tracker.ui.analytics.month

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.R
import start.up.tracker.databinding.FragmentAnalyticsMonthBinding
import java.util.*

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

        val cartesian = AnyChart.column()
        val column = cartesian.column(viewModel.data)

        column.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(5.0)
            .offsetY(5.0)
            .format("Tasks: {%Value}{groupsSeparator: }");


        cartesian.xAxis(0).title(viewModel.currentMonthName);

        cartesian.title("Completed tasks")
        cartesian.title().fontSize(12)
        cartesian.title().fontColor("#858585")


        cartesian.animation(true)
        cartesian.yScale().minimumGap(1)


        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)

        binding.lineChartMonth.setChart(cartesian)
    }
}