package start.up.tracker.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
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
import start.up.tracker.data.entities.ExtendedTask
import start.up.tracker.data.entities.UpcomingSection
import start.up.tracker.databinding.FragmentUpcomingBinding
import start.up.tracker.mvvm.view_models.upcoming.UpcomingViewModel
import start.up.tracker.ui.data.entities.TasksEvent
import start.up.tracker.ui.list.adapters.UpcomingAdapter
import start.up.tracker.utils.toTask

@AndroidEntryPoint
class UpcomingFragment : Fragment(R.layout.fragment_upcoming) {

    private val viewModel: UpcomingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentUpcomingBinding.bind(view)

        val upcomingAdapter = UpcomingAdapter(viewModel)

        binding.upcomingRV.apply {
            adapter = upcomingAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.upcomingTasks.observe(viewLifecycleOwner) { tasks ->
            separateDataAndSubmit(tasks, upcomingAdapter)
        }

        setFragmentResultListener("add_edit_request") { _, bundle ->
            val result = bundle.getInt("add_edit_result")
            viewModel.onAddEditResult(result)
        }

        binding.addTask.setOnClickListener {
            viewModel.onAddNewTaskClick()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.tasksEvent.collect { event ->
                when (event) {
                    is TasksEvent.ShowUndoDeleteExtendedTaskMessage -> {
                        Snackbar.make(requireView(), "Task deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                viewModel.onUndoDeleteExtendedTaskClick(event.extendedTask)
                            }.show()
                    }
                    is TasksEvent.NavigateToAddTaskScreen -> {
                        val action = UpcomingFragmentDirections.actionUpcomingToAddEditTask(title = "Add new task", categoryId = 1)
                        findNavController().navigate(action)
                    }
                    is TasksEvent.NavigateToEditExtendedTaskScreen -> {
                        val task = event.extendedTask.toTask()
                        val action = UpcomingFragmentDirections.actionUpcomingToAddEditTask(title = "Edit task", categoryId = event.extendedTask.categoryId, task = task)
                        findNavController().navigate(action)
                    }
                    is TasksEvent.ShowTaskSavedConfirmationMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                    is TasksEvent.NavigateToDeleteAllCompletedScreen -> {
                        val action = ProjectsTasksFragmentDirections.actionGlobalDeleteAllCompletedDialog()
                        findNavController().navigate(action)
                    }
                }
            }
        }

        setHasOptionsMenu(true)
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
}
