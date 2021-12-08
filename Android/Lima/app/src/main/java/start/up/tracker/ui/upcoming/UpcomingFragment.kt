package start.up.tracker.ui.upcoming

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.R
import start.up.tracker.data.models.ExtendedTask
import start.up.tracker.data.models.UpcomingSection
import start.up.tracker.databinding.FragmentUpcomingBinding
import start.up.tracker.utils.DEFAULT_PROJECT_COLOR

@AndroidEntryPoint
class UpcomingFragment : Fragment(R.layout.fragment_upcoming) {

    private val viewModel: UpcomingViewModel by viewModels()

    val tasksList: List<ExtendedTask> = listOf(
        ExtendedTask(10,"Wash dishes", 1, false, 22, "14.15.2022",1640304000000, "14:00", "15:00", 340, 500, "Eng", DEFAULT_PROJECT_COLOR, 4),
        ExtendedTask(11,"Wash dishes", 2, false, 22, "14.15.2022",1640304000000, "14:00", "15:00", 340, 500, "Eng", DEFAULT_PROJECT_COLOR, 4),
        ExtendedTask(12,"Wash dishes", 3, false, 22, "14.15.2022",1640044800000, "14:00", "15:00", 340, 500, "Eng", DEFAULT_PROJECT_COLOR, 4),
        ExtendedTask(13,"Wash dishes", 0, false, 22, "14.15.2022", 1640044800000,"14:00", "15:00", 340, 500, "Eng", DEFAULT_PROJECT_COLOR, 4),
    )

    val list: List<UpcomingSection> = listOf(
        UpcomingSection("24 January", tasksList),
        UpcomingSection("27 January", tasksList),
        UpcomingSection("31 January", tasksList)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentUpcomingBinding.bind(view)

        val upcomingAdapter = UpcomingAdapter()
        binding.upcomingRV.apply {
            adapter = upcomingAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        upcomingAdapter.submitList(list)

        viewModel.upcomingTasks.observe(viewLifecycleOwner) {
            Log.i("upcoming", it.toString())
        }
    }
}