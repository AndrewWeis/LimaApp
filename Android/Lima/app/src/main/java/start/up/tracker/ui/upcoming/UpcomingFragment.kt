package start.up.tracker.ui.upcoming

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.R
import start.up.tracker.data.models.ExtendedTask
import start.up.tracker.data.models.UpcomingSection
import start.up.tracker.databinding.FragmentUpcomingBinding

@AndroidEntryPoint
class UpcomingFragment : Fragment(R.layout.fragment_upcoming) {

    private val viewModel: UpcomingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentUpcomingBinding.bind(view)

        val upcomingAdapter = UpcomingAdapter()

        binding.upcomingRV.apply {
            adapter = upcomingAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }


        viewModel.upcomingTasks.observe(viewLifecycleOwner) { tasks ->
            separateDataAndSubmit(tasks, upcomingAdapter)
        }
    }

    private fun separateDataAndSubmit(tasks: List<ExtendedTask>, adapter: UpcomingAdapter) {
        val sectionsList: MutableList<UpcomingSection> = mutableListOf()
        val tasksList: MutableList<ExtendedTask> = mutableListOf()

        for (i in tasks.indices) {
            tasksList.add(tasks[i])
            if (i+1 == tasks.size || tasks[i].dateLong != tasks[i+1].dateLong) {
                sectionsList.add(UpcomingSection(tasks[i].date, tasksList.toList()))
                tasksList.clear()
            }
        }

        adapter.submitList(sectionsList.toList())
    }
}