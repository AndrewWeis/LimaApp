package start.up.tracker.ui.view_pagers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import start.up.tracker.ui.fragments.calendar.CalendarFragment
import start.up.tracker.ui.fragments.today.TodayTasksFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                TodayTasksFragment()
            }
            1 -> {
                CalendarFragment()
            }
            else -> {
                Fragment()
            }
        }
    }
}
