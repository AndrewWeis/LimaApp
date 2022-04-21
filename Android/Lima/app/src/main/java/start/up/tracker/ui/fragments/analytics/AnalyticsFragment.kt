package start.up.tracker.ui.fragments.analytics

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.R
import start.up.tracker.databinding.FragmentAnalyticsBinding
import start.up.tracker.ui.view_pagers.AnalyticsViewPagerAdapter

@AndroidEntryPoint
class AnalyticsFragment :
    Fragment(R.layout.fragment_analytics) {

    private var binding: FragmentAnalyticsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAnalyticsBinding.bind(view)

        initViewPager()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun initViewPager() {
        val adapter = AnalyticsViewPagerAdapter(childFragmentManager, lifecycle)

        binding?.viewPager2?.adapter = adapter

        TabLayoutMediator(binding!!.tabLayout, binding!!.viewPager2) { tab, position ->
            when (position) {
                0 -> { tab.text = "Week" }
                1 -> { tab.text = "Month" }
            }
        }.attach()
    }
}
