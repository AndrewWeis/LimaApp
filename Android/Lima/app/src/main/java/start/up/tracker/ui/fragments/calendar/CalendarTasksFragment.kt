package start.up.tracker.ui.fragments.calendar

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import start.up.tracker.R
import start.up.tracker.databinding.FragmentCalendarTasksBinding
import start.up.tracker.entities.Task
import start.up.tracker.mvvm.view_models.today.CalendarTasksViewModel
import start.up.tracker.ui.data.constants.TIME_OFFSET
import start.up.tracker.ui.data.entities.TasksEvent
import start.up.tracker.ui.extensions.list.ListExtension
import start.up.tracker.ui.fragments.tasks.ProjectTasksFragmentDirections
import start.up.tracker.ui.fragments.tasks.base.BaseTasksFragment
import start.up.tracker.ui.fragments.today.TodayFragmentDirections
import start.up.tracker.ui.list.adapters.calendar.CalendarTasksAdapter
import start.up.tracker.ui.list.generators.calendar.CalendarTasksGenerator
import start.up.tracker.ui.list.view_holders.tasks.OnTaskClickListener
import start.up.tracker.utils.TimeHelper
import start.up.tracker.utils.convertDpToPx

@AndroidEntryPoint
class CalendarTasksFragment :
    BaseTasksFragment(R.layout.fragment_calendar_tasks),
    OnTaskClickListener {

    private val viewModel: CalendarTasksViewModel by viewModels()

    private var binding: FragmentCalendarTasksBinding? = null

    private lateinit var adapter: CalendarTasksAdapter
    private var listExtension: ListExtension? = null
    private val generator = CalendarTasksGenerator()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCalendarTasksBinding.bind(view)

        initAdapter()
        initObservers()
        initTaskEventListener()
        initCurrentTimeIndicator()

        setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        listExtension = null
    }

    override fun onTaskClick(task: Task) {
        viewModel.onTaskSelected(task)
    }

    override fun onCheckBoxClick(task: Task) {
        viewModel.onTaskCheckedChanged(task)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_today_tasks, menu)

        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.action_hide_completed_tasks).isChecked =
                viewModel.hideCompleted.first()
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

    private fun showTasks(tasks: List<Task>) {
        adapter.addListItems(generator.createListItems(tasks))
    }

    private fun initTaskEventListener() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.tasksEvent.collect { event ->
            when (event) {
                is TasksEvent.ShowUndoDeleteTaskMessage -> {
                    showUndoDeleteSnackbar { viewModel.onUndoDeleteTaskClick(event.task) }
                }
                is TasksEvent.NavigateToAddTaskScreen -> {
                    val action = TodayFragmentDirections.actionTodayToAddEditTask(
                        title = "Add new task",
                        categoryId = 1
                    )
                    navigateTo(action)
                }
                is TasksEvent.NavigateToEditTaskScreen -> {
                    val task = event.task
                    val action = TodayFragmentDirections.actionTodayToAddEditTask(
                        title = "Edit task",
                        categoryId = event.task.categoryId,
                        task = task
                    )
                    navigateTo(action)
                }
                is TasksEvent.NavigateToDeleteAllCompletedScreen -> {
                    val action =
                        ProjectTasksFragmentDirections.actionGlobalDeleteAllCompletedDialog()
                    navigateTo(action)
                }
            }
        }
    }

    private fun initObservers() {
        viewModel.calendarTasks.observe(viewLifecycleOwner) {
            showTasks(it)
        }
    }

    private fun initAdapter() {
        adapter = CalendarTasksAdapter(
            layoutInflater = layoutInflater,
            listener = this
        )

        listExtension = ListExtension(binding?.calendarList)
        listExtension?.setVerticalLayoutManager()
        listExtension?.setAdapter(adapter)
    }

    private fun initCurrentTimeIndicator() {
        val layoutParams = binding?.timeIndicatorView?.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin =
            convertDpToPx(TimeHelper.getMinutesOfCurrentDay() - TIME_OFFSET.toInt())
        binding?.timeIndicatorView?.requestLayout()
    }
}
