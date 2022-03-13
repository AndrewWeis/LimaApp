package start.up.tracker.ui.fragments.upcoming

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import start.up.tracker.R
import start.up.tracker.entities.Task
import start.up.tracker.entities.UpcomingSection
import start.up.tracker.databinding.FragmentUpcomingBinding
import start.up.tracker.mvvm.view_models.upcoming.UpcomingViewModel
import start.up.tracker.ui.data.entities.TasksEvent
import start.up.tracker.ui.fragments.BaseTasksFragment
import start.up.tracker.ui.fragments.tasks.ProjectsTasksFragmentDirections
import start.up.tracker.ui.list.adapters.UpcomingAdapter
import start.up.tracker.utils.TimeHelper

@AndroidEntryPoint
class UpcomingFragment :
    BaseTasksFragment(R.layout.fragment_upcoming) {

    private val viewModel: UpcomingViewModel by viewModels()

    private var binding: FragmentUpcomingBinding? = null
    private lateinit var upcomingAdapter: UpcomingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUpcomingBinding.bind(view)

        initAdapter()
        initListeners()
        initResultListeners()
        initViewModelObservers()
        initTaskEventListeners()

        setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_today_tasks, menu)

        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.action_hide_completed_tasks).isChecked =
                viewModel.hideCompleted.first() ?: false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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

    private fun initTaskEventListeners() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.tasksEvent.collect { event ->
            when (event) {
                is TasksEvent.ShowUndoDeleteTaskMessage -> {
                    showUndoDeleteSnackbar { viewModel.onUndoDeleteTaskClick(event.task) }
                }
                is TasksEvent.NavigateToAddTaskScreen -> {
                    val action = UpcomingFragmentDirections.actionUpcomingToAddEditTask(
                        title = "Add new task",
                        categoryId = 1
                    )
                    navigateTo(action)
                }
                is TasksEvent.NavigateToEditTaskScreen -> {
                    val task = event.task
                    val action = UpcomingFragmentDirections.actionUpcomingToAddEditTask(
                        title = "Edit task",
                        categoryId = event.task.categoryId,
                        task = task
                    )
                    navigateTo(action)
                }
                is TasksEvent.ShowTaskSavedConfirmationMessage -> {
                    showTaskSavedMessage(event.msg)
                }
                is TasksEvent.NavigateToDeleteAllCompletedScreen -> {
                    val action =
                        ProjectsTasksFragmentDirections.actionGlobalDeleteAllCompletedDialog()
                    navigateTo(action)
                }
            }
        }
    }

    private fun initListeners() {
        binding?.addTask?.setOnClickListener {
            viewModel.onAddNewTaskClick()
        }
    }

    private fun initViewModelObservers() {
        viewModel.upcomingTasks.observe(viewLifecycleOwner) { tasks ->
            separateDataAndSubmit(tasks, upcomingAdapter)
        }
    }

    private fun initResultListeners() {
        setFragmentResultListener("add_edit_request") { _, bundle ->
            val result = bundle.getInt("add_edit_result")
            viewModel.onAddEditResult(result)
        }
    }

    private fun initAdapter() {
        upcomingAdapter = UpcomingAdapter(viewModel)

        binding?.upcomingRV?.apply {
            adapter = upcomingAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun separateDataAndSubmit(tasks: List<Task>, adapter: UpcomingAdapter) {
        val sectionsList: MutableList<UpcomingSection> = mutableListOf()
        val tasksList: MutableList<Task> = mutableListOf()

        for (i in tasks.indices) {
            tasksList.add(tasks[i])
            if (i + 1 == tasks.size || tasks[i].date != tasks[i + 1].date) {
                sectionsList.add(
                    UpcomingSection(
                        section = TimeHelper.formatMillisecondToDate(
                            tasks[i].date,
                            TimeHelper.DateFormats.DD_MMMM
                        ),
                        tasksList = tasksList.toList()
                    )
                )
                tasksList.clear()
            }
        }

        adapter.submitList(sectionsList.toList())
    }
}
