package start.up.tracker.ui.analytics.year

import android.os.Bundle
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
import start.up.tracker.databinding.FragmentAnalyticsYearBinding
import start.up.tracker.ui.addedittask.AddEditTaskViewModel
import java.util.*

@AndroidEntryPoint
class AnalyticsYearFragment : Fragment(R.layout.fragment_analytics_year) {

    private val viewModel: AnalyticsYearViewModel by viewModels()
    private lateinit var binding: FragmentAnalyticsYearBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAnalyticsYearBinding.bind(view)

        binding.LineChart.setProgressBar(binding.progressBar)

        viewModel.statYear.observe(viewLifecycleOwner) {
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


        cartesian.xAxis(0).title(viewModel.currentYear);

        cartesian.title("Completed tasks")
        cartesian.title().fontSize(12)
        cartesian.title().fontColor("#858585")


        cartesian.animation(true)
        cartesian.yScale().maximumGap(1)


        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)

        binding.LineChart.setChart(cartesian)
    }
}