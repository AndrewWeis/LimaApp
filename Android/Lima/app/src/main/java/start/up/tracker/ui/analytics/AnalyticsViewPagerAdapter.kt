package start.up.tracker.ui.analytics

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import start.up.tracker.ui.analytics.fragments.AnalyticsAllFragment
import start.up.tracker.ui.analytics.fragments.AnalyticsDayFragment
import start.up.tracker.ui.analytics.fragments.AnalyticsMonthFragment
import start.up.tracker.ui.analytics.fragments.AnalyticsWeekFragment

class AnalyticsViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> { AnalyticsDayFragment() }
            1 -> { AnalyticsWeekFragment() }
            2 -> { AnalyticsMonthFragment() }
            3 -> { AnalyticsAllFragment() }
            else -> { Fragment() }
        }
    }
}