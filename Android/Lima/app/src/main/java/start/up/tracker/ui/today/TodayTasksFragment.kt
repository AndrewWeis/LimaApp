package start.up.tracker.ui.today

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.R
import start.up.tracker.data.models.Task
import start.up.tracker.data.models.TodayTask
import start.up.tracker.databinding.FragmentTodayTasksBinding
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class TodayTasksFragment : Fragment(R.layout.fragment_today_tasks), TodayTasksAdapter.OnItemClickListener {

    private val viewModel: TodayViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTodayTasksBinding.bind(view)

        val formatter = SimpleDateFormat("dd.MM.yyyy")
        val currentDate: String = formatter.format(Date())

        binding.txTaskToday.text = currentDate

        val taskAdapter = TodayTasksAdapter(this)
        binding.todayTaskRV.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        viewModel.todayTasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }

    }

    override fun onItemClick(todayTask: TodayTask) {
        //   viewModel.onTaskSelected(task)
    }

    override fun onCheckBoxClick(todayTask: TodayTask, isChecked: Boolean) {
        // viewModel.onTaskCheckedChanged(task, isChecked)
    }
}