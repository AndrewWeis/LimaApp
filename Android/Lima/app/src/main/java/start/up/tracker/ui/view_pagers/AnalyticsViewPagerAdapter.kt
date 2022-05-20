package start.up.tracker.ui.view_pagers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import start.up.tracker.ui.fragments.analytics.AnalyticsYearFragment
import start.up.tracker.ui.fragments.analytics.AnalyticsMonthFragment
import start.up.tracker.ui.fragments.analytics.AnalyticsWeekFragment

class AnalyticsViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                AnalyticsWeekFragment()
            }
            1 -> {
                AnalyticsMonthFragment()
            }
            2 -> {
                AnalyticsYearFragment()
            }
            else -> {
                Fragment()
            }
        }
    }
}