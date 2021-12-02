package start.up.tracker.ui.today

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.R
import start.up.tracker.databinding.FragmentTodayBinding
import start.up.tracker.ui.projectstasks.ProjectsTasksViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class TodayFragment : Fragment(R.layout.fragment_today) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTodayBinding.bind(view)

        binding.apply {
            val adapter = ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
            viewPager2.adapter = adapter

            TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
                when(position) {
                    0 -> tab.text = "Задачи"
                    1 -> tab.text = "Календарь"
                }
            }.attach()
        }
    }
}