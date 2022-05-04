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
import start.up.tracker.databinding.CalendarFragmentBinding
import start.up.tracker.entities.Task
import start.up.tracker.mvvm.view_models.today.CalendarViewModel
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.TasksEvent
import start.up.tracker.ui.data.entities.calendar.CalendarTasksData
import start.up.tracker.ui.data.entities.calendar.CalendarTimesData
import start.up.tracker.ui.extensions.list.ListExtension
import start.up.tracker.ui.fragments.tasks.ProjectTasksFragmentDirections
import start.up.tracker.ui.fragments.tasks.base.BaseTasksFragment
import start.up.tracker.ui.fragments.today.TodayFragmentDirections
import start.up.tracker.ui.list.adapters.calendar.CalendarTasksAdapter
import start.up.tracker.ui.list.adapters.calendar.CalendarTimesAdapter
import start.up.tracker.ui.list.view_holders.tasks.OnTaskClickListener
import start.up.tracker.utils.TimeHelper
import start.up.tracker.utils.resources.ResourcesUtils

@AndroidEntryPoint
class CalendarFragment :
    BaseTasksFragment(R.layout.calendar_fragment),
    OnTaskClickListener {

    private val viewModel: CalendarViewModel by viewModels()

    private var binding: CalendarFragmentBinding? = null

    private lateinit var calendarTimesAdapter: CalendarTimesAdapter
    private var calendarTimesListExtension: ListExtension? = null

    private lateinit var calendarTasksAdapter: CalendarTasksAdapter
    private var calendarTasksListExtension: ListExtension? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CalendarFragmentBinding.bind(view)

        initCalendarTimesAdapter()
        initCalendarTasksAdapter()
        initObservers()
        initTaskEventListener()
        initCurrentTimeIndicator()

        setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        calendarTimesListExtension = null
        calendarTasksListExtension = null
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

    private fun showTasks(calendarTasksData: CalendarTasksData) {
        val tasksListItems = calendarTasksData.values.map { ListItem(data = it) }
        calendarTasksAdapter.updateItems(tasksListItems)
    }

    private fun showTimes(calendarTimes: CalendarTimesData) {
        val timesListItems = calendarTimes.values.map { ListItem(data = it) }
        calendarTimesAdapter.updateItems(timesListItems)
    }

    private fun initTaskEventListener() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.tasksEvent.collect { event ->
            when (event) {
                is TasksEvent.ShowUndoDeleteTaskMessage -> {
                    showUndoDeleteSnackbar {
                        viewModel.onUndoDeleteTaskClick(
                            event.task,
                            event.subtasks
                        )
                    }
                }
                is TasksEvent.NavigateToAddTaskScreen -> {
                    val action = TodayFragmentDirections.actionTodayToAddEditTask(
                        title = "Add new task",
                        projectId = 1
                    )
                    navigateTo(action)
                }
                is TasksEvent.NavigateToEditTaskScreen -> {
                    val task = event.task
                    val action = TodayFragmentDirections.actionTodayToAddEditTask(
                        title = "Edit task",
                        projectId = event.task.projectId,
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
            showTasks(CalendarTasksData(values = it))
        }

        viewModel.calendarTimes.observe(viewLifecycleOwner) {
            showTimes(it)
        }
    }

    private fun initCalendarTimesAdapter() {
        calendarTimesAdapter = CalendarTimesAdapter(
            layoutInflater = layoutInflater,
        )

        calendarTimesListExtension = ListExtension(binding?.calendarTimesList)
        calendarTimesListExtension?.setVerticalLayoutManager()
        calendarTimesListExtension?.setAdapter(calendarTimesAdapter)
    }

    private fun initCalendarTasksAdapter() {
        calendarTasksAdapter = CalendarTasksAdapter(
            layoutInflater = layoutInflater,
            listener = this
        )

        calendarTasksListExtension = ListExtension(binding?.calendarTasksList)
        calendarTasksListExtension?.setVerticalLayoutManager()
        calendarTasksListExtension?.setAdapter(calendarTasksAdapter)
    }

    private fun initCurrentTimeIndicator() {
        val layoutParams = binding?.timeIndicatorView?.layoutParams as ViewGroup.MarginLayoutParams
        val topMarginInDp = TimeHelper.getMinutesOfCurrentDay() + CALENDAR_MARGIN_TOP
        val topMarginInPx = ResourcesUtils.getPxByDp(topMarginInDp)

        layoutParams.topMargin = topMarginInPx
        binding?.timeIndicatorView?.requestLayout()

        scrollToTimeIndicator(topMarginInDp)
    }

    private fun scrollToTimeIndicator(indicatorTopMarginInDp: Float) {
        val layoutParams = binding?.scrollOffsetView?.layoutParams as ViewGroup.MarginLayoutParams
        var offsetTopMarginInDp = OFFSET_TOP + indicatorTopMarginInDp

        if (indicatorTopMarginInDp > (LAYOUT_HEIGHT_TOTAL - OFFSET_TOP)) {
            offsetTopMarginInDp = LAYOUT_HEIGHT_TOTAL + CALENDAR_MARGIN_TOP
        }
        val offsetTopMarginInPx = ResourcesUtils.getPxByDp(offsetTopMarginInDp)

        layoutParams.topMargin = offsetTopMarginInPx
        binding?.scrollOffsetView?.requestLayout()

        binding?.calendarScroll?.apply {
            post { smoothScrollTo(0, binding?.scrollOffsetView!!.bottom, ANIMATION_DURATION) }
        }
    }

    companion object {
        const val CALENDAR_MARGIN_TOP = 16F + 2F
        const val LAYOUT_HEIGHT_TOTAL = 1560F
        const val ANIMATION_DURATION = 2000
        const val OFFSET_TOP = 300F
    }
}
