package start.up.tracker.ui.fragments.calendar

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import start.up.tracker.R
import start.up.tracker.ui.data.constants.TIME_OFFSET
import start.up.tracker.data.entities.Task
import start.up.tracker.databinding.FragmentCalendarTasksBinding
import start.up.tracker.mvvm.view_models.today.CalendarTasksViewModel
import start.up.tracker.ui.data.entities.TasksEvent
import start.up.tracker.ui.fragments.BaseTasksFragment
import start.up.tracker.ui.fragments.tasks.ProjectsTasksFragmentDirections
import start.up.tracker.ui.fragments.today.TodayFragmentDirections
import start.up.tracker.ui.list.adapters.CalendarTasksAdapter
import start.up.tracker.utils.convertDpToPx
import start.up.tracker.utils.timeToMinutes
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CalendarTasksFragment :
    BaseTasksFragment(R.layout.fragment_calendar_tasks),
    CalendarTasksAdapter.OnItemClickListener {

    private val viewModel: CalendarTasksViewModel by viewModels()

    private var binding: FragmentCalendarTasksBinding? = null
    private lateinit var taskAdapter: CalendarTasksAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCalendarTasksBinding.bind(view)

        initAdapter()
        initListeners()
        initTaskEventListener()
        initCurrentTimeIndicator()

        setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onItemClick(task: Task) {
        viewModel.onTaskSelected(task)
    }

    override fun onCheckBoxClick(task: Task, isChecked: Boolean) {
        viewModel.onTaskCheckedChanged(task, isChecked)
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
        viewModel.calendarTasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }
    }

    private fun initAdapter() {
        taskAdapter = CalendarTasksAdapter(this)

        binding?.todayCalendarRV?.apply {
            itemAnimator = null
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun initCurrentTimeIndicator() {
        val sdf = SimpleDateFormat("HH:mm")
        val currentDate = sdf.format(Date())
        val minutes = timeToMinutes(currentDate)

        val layoutParams: ViewGroup.MarginLayoutParams =
            binding?.currentTime?.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin = convertDpToPx(minutes - TIME_OFFSET)
        binding?.currentTime?.requestLayout()
    }
}
