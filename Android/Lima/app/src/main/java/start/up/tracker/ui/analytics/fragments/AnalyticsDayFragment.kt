package start.up.tracker.ui.analytics.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import start.up.tracker.R
import start.up.tracker.databinding.FragmentAnalyticsBinding
import start.up.tracker.databinding.FragmentAnalyticsDayBinding
import java.util.ArrayList


class AnalyticsDayFragment : Fragment(R.layout.fragment_analytics_day) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAnalyticsDayBinding.bind(view)

        binding.LineChart.setProgressBar(binding.progressBar)

        val cartesian = AnyChart.column()

        val data: MutableList<DataEntry> = ArrayList()
        data.add(ValueDataEntry("05:00", 0))
        data.add(ValueDataEntry("06:00", 1))
        data.add(ValueDataEntry("07:00", 3))
        data.add(ValueDataEntry("08:00", 0))
        data.add(ValueDataEntry("09:00", 0))
        data.add(ValueDataEntry("10:00", 1))
        data.add(ValueDataEntry("11:00", 1))
        data.add(ValueDataEntry("12:00", 0))
        data.add(ValueDataEntry("13:00", 1))
        data.add(ValueDataEntry("14:00", 1))
        data.add(ValueDataEntry("15:00", 1))
        data.add(ValueDataEntry("16:00", 1))
        data.add(ValueDataEntry("17:00", 0))
        data.add(ValueDataEntry("18:00", 1))
        data.add(ValueDataEntry("19:00", 1))
        data.add(ValueDataEntry("20:00", 0))
        data.add(ValueDataEntry("21:00", 0))
        data.add(ValueDataEntry("22:00", 0))
        data.add(ValueDataEntry("23:00", 0))

        val column = cartesian.column(data)

        column.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(5.0)
            .offsetY(5.0)
            .format("Tasks: {%Value}{groupsSeparator: }");


        cartesian.title("Количество выполненных задач")
        cartesian.title().fontSize(12)
        cartesian.title().fontColor("#858585")


        cartesian.animation(true)
        cartesian.yScale().maximumGap(0)


        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)

        binding.LineChart.setChart(cartesian)
    }
}