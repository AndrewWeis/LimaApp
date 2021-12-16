package start.up.tracker.ui.analytics

import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.data.Mapping
import com.anychart.data.Set
import com.anychart.enums.*
import com.anychart.graphics.vector.Stroke
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_analytics.view.*
import start.up.tracker.R
import start.up.tracker.databinding.FragmentAnalyticsBinding
import start.up.tracker.ui.today.ViewPagerAdapter
import java.util.ArrayList

class AnalyticsFragment : Fragment(R.layout.fragment_analytics) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAnalyticsBinding.bind(view)

        val adapter = AnalyticsViewPagerAdapter(childFragmentManager, lifecycle)

        binding.viewPager2.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            when(position) {
                0 -> { tab.text = "Day" }
                1 -> { tab.text = "Week" }
                2 -> { tab.text = "Month" }
                3 -> { tab.text = "All time" }
            }
        }.attach()
    }
 }
