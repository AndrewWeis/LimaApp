package start.up.tracker.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import start.up.tracker.R
import start.up.tracker.data.constants.TIME_OFFSET
import start.up.tracker.data.entities.ExtendedTask
import start.up.tracker.databinding.FragmentCalendarBinding
import start.up.tracker.mvvm.view_models.tasks.TasksViewModel
import start.up.tracker.ui.list.adapters.CalendarTasksAdapter
import start.up.tracker.mvvm.view_models.today.TodayViewModel
import start.up.tracker.utils.*
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CalendarFragment : Fragment(R.layout.fragment_calendar), CalendarTasksAdapter.OnItemClickListener {

    private val viewModel: TodayViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCalendarBinding.bind(view)

        val taskAdapter = CalendarTasksAdapter(this)
        binding.todayCalendarRV.apply {
            itemAnimator = null
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        /*binding.addTaskOfToday.setOnClickListener {
            viewModel.onAddNewTaskClick()
        }*/

        viewModel.calendarTasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }

        setUpCurrentTimeIndicator(binding)



        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.tasksEvent.collect { event ->
                when (event) {
                    is TasksViewModel.TasksEvent.ShowUndoDeleteTaskMessage -> {
                        Snackbar.make(requireView(), "Task deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                viewModel.onUndoDeleteClick(event.extendedTask)
                            }.show()
                    }
                    is TasksViewModel.TasksEvent.NavigateToAddTaskScreen -> {
                        val action = TodayFragmentDirections.actionTodayToAddEditTask(title = "Add new task", categoryId = 1)
                        findNavController().navigate(action)
                    }
                    is TasksViewModel.TasksEvent.NavigateToEditTaskScreen -> {
                        val task = event.extendedTask.toTask()
                        val action = TodayFragmentDirections.actionTodayToAddEditTask(title = "Edit task", categoryId = event.extendedTask.categoryId, task = task)
                        findNavController().navigate(action)
                    }
                    is TasksViewModel.TasksEvent.ShowTaskSavedConfirmationMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                    is TasksViewModel.TasksEvent.NavigateToDeleteAllCompletedScreen -> {
                        val action = ProjectsTasksFragmentDirections.actionGlobalDeleteAllCompletedDialog()
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }

        setHasOptionsMenu(true)
    }

    private fun setUpCurrentTimeIndicator(binding: FragmentCalendarBinding) {
        val sdf = SimpleDateFormat("HH:mm")
        val currentDate = sdf.format(Date())
        val minutes = timeToMinutes(currentDate)

        val layoutParams: ViewGroup.MarginLayoutParams = binding.currentTime.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin = convertDpToPx(minutes - TIME_OFFSET)
        binding.currentTime.requestLayout()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_today_tasks, menu)

        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.action_hide_completed_tasks).isChecked =
                viewModel.hideCompleted.first() ?: false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_hide_completed_tasks -> {
                item.isChecked = !item.isChecked
                viewModel.onHideCompletedClick(item.isChecked)
                true
            }
            R.id.action_delete_all_completed_tasks -> {
                viewModel.onDeleteAllCompletedClick()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(extendedTask: ExtendedTask) {
        viewModel.onTaskSelected(extendedTask)
    }

    override fun onCheckBoxClick(extendedTask: ExtendedTask, isChecked: Boolean) {
        viewModel.onTaskCheckedChanged(extendedTask, isChecked)
    }
}