package start.up.tracker.ui.fragments.today

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import start.up.tracker.R
import start.up.tracker.databinding.FragmentTodayBinding
import start.up.tracker.mvvm.view_models.today.TodayViewModel
import start.up.tracker.ui.data.entities.TasksEvent
import start.up.tracker.ui.view_pagers.ViewPagerAdapter

@AndroidEntryPoint
class TodayFragment : Fragment(R.layout.fragment_today) {

    private val viewModel: TodayViewModel by viewModels()

    private var binding: FragmentTodayBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTodayBinding.bind(view)

        initResultListener()
        initTaskEventListener()
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

    private fun initTaskEventListener() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.tasksEvent.collect { event ->
                when (event) {
                    is TasksEvent.ShowTaskSavedConfirmationMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun initResultListener() {
        setFragmentResultListener("add_edit_request") { _, bundle ->
            val result = bundle.getInt("add_edit_result")
            viewModel.onAddEditResult(result)
        }
    }
}
