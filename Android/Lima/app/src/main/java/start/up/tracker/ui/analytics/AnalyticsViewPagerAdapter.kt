package start.up.tracker.ui.analytics

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import start.up.tracker.ui.analytics.year.AnalyticsYearFragment
import start.up.tracker.ui.analytics.month.AnalyticsMonthFragment
import start.up.tracker.ui.analytics.week.AnalyticsWeekFragment

class AnalyticsViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> { AnalyticsMonthFragment() }
            1 -> { AnalyticsYearFragment()}
            2 -> { AnalyticsWeekFragment() }
            else -> { Fragment() }
        }
    }
}