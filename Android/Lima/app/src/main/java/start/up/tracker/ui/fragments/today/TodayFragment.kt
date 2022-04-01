package start.up.tracker.ui.fragments.today

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.R
import start.up.tracker.databinding.FragmentTodayBinding
import start.up.tracker.ui.view_pagers.ViewPagerAdapter

@AndroidEntryPoint
class TodayFragment : Fragment(R.layout.fragment_today) {

    private var binding: FragmentTodayBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTodayBinding.bind(view)

        initAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun initAdapter() {
        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        binding?.viewPager2?.adapter = adapter

        TabLayoutMediator(binding!!.tabLayout, binding!!.viewPager2) { tab, position ->
            when (position) {
                0 -> { tab.text = "Tasks" }
                1 -> { tab.text = "Calendar" }
            }
        }.attach()
    }
}
