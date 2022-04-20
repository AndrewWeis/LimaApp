package start.up.tracker.ui.fragments.edit_task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import start.up.tracker.R
import start.up.tracker.databinding.EditTaskFragmentBinding
import start.up.tracker.entities.Task
import start.up.tracker.mvvm.view_models.edit_task.EditTaskViewModel
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.TasksEvent
import start.up.tracker.ui.data.entities.edit_task.ActionIcon
import start.up.tracker.ui.data.entities.edit_task.ActionIcons
import start.up.tracker.ui.data.entities.header.HeaderActions
import start.up.tracker.ui.data.entities.tasks.TasksData
import start.up.tracker.ui.extensions.list.ListExtension
import start.up.tracker.ui.fragments.base.BaseBottomSheetDialogFragment
import start.up.tracker.ui.list.adapters.edit_task.EditTaskAdapter
import start.up.tracker.ui.list.generators.edit_task.EditTaskInfoGenerator
import start.up.tracker.ui.list.view_holders.add_project.HeaderActionsViewHolder
import start.up.tracker.ui.list.view_holders.edit_task.ActionIconViewHolder
import start.up.tracker.ui.list.view_holders.tasks.AddSubtaskViewHolder
import start.up.tracker.ui.list.view_holders.tasks.OnTaskClickListener
import start.up.tracker.ui.views.forms.base.BaseInputView
import start.up.tracker.utils.TimeHelper
import start.up.tracker.utils.resources.ResourcesUtils
import start.up.tracker.utils.screens.ExtraCodes
import java.util.*

@AndroidEntryPoint
class EditTaskFragment :
    BaseBottomSheetDialogFragment(R.layout.edit_task_fragment),
    BaseInputView.TextInputListener,
    TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener,
    OnTaskClickListener,
    AddSubtaskViewHolder.OnAddSubtaskClickListener,
    ActionIconViewHolder.ActionIconClickListener,
    HeaderActionsViewHolder.AddProjectActionClickListener {

    private val viewModel: EditTaskViewModel by viewModels()

    private var binding: EditTaskFragmentBinding? = null

    private lateinit var adapter: EditTaskAdapter
    private var listExtension: ListExtension? = null
    private val generator: EditTaskInfoGenerator = EditTaskInfoGenerator()

    // todo(костыльное решение, подумать как можно сделать по-другому)
    private var isTaskTimeTypeStart = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = EditTaskFragmentBinding.bind(view)

        initAdapter()
        initObservers()
        initResultListeners()
        initEventsListener()
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveDataAboutSubtask()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        listExtension = null
    }

    override fun onDoneButtonClick() {
        viewModel.onSaveClick()
    }

    override fun onBackButtonClick() {
        viewModel.onBackButtonClick()
    }

    override fun onTextInputDataChange(listItem: ListItem) {
        val value = listItem.data as String
        when (listItem.id) {
            ListItemIds.TASK_TITLE -> viewModel.onTaskTitleChanged(value)
            ListItemIds.TASK_DESCRIPTION -> viewModel.onTaskDescriptionChanged(value)
        }
    }

    override fun onFocusLose(listItem: ListItem) {
        hideKeyboard()
        binding?.content?.requestFocus()
    }

    override fun onFocusGain(listItem: ListItem) {
        /*
         * Код добавлен, так как не всегда при захвате фокуса показывается клавиатура.
         * Например, когда используется функция copy-paste
         */
        showKeyboard()
    }

    override fun onClearClick(listItem: ListItem) {
        when (listItem.id) {
            ListItemIds.TASK_TITLE -> viewModel.onTaskTitleClearClick()
            ListItemIds.TASK_DESCRIPTION -> viewModel.onTaskDescriptionClearClick()
        }
    }

    override fun onDoneClick(item: ListItem) {
        hideKeyboard()
    }

    override fun onTaskClick(task: Task) {
        viewModel.onTaskSelected(task)
    }

    override fun onCheckBoxClick(task: Task) {
        viewModel.onTaskCheckedChanged(task)
    }

    override fun onAddSubtaskClick() {
        viewModel.onAddSubtaskClick()
    }

    override fun onTimeSet(timePicker: TimePicker, hours: Int, minutes: Int) {
        if (isTaskTimeTypeStart) {
            viewModel.onTaskStartTimeChanged(hours * 60 + minutes)
        } else {
            viewModel.onTaskEndTimeChanged(hours * 60 + minutes)
        }
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val milliseconds = TimeHelper.getDateInMilliseconds(year, month, dayOfMonth)
        viewModel.onTaskDateChanged(milliseconds)
    }

    override fun onActionClickListener(actionIconId: Int) {
        when (actionIconId) {
            ActionIcon.ICON_PRIORITY -> viewModel.onIconPriorityClick()
            ActionIcon.ICON_DATE -> viewModel.onIconDateClick()
            ActionIcon.ICON_TIME_START -> viewModel.onIconTimeStartClick()
            ActionIcon.ICON_TIME_END -> viewModel.onIconTimeEndClick()
            ActionIcon.ICON_PROJECTS -> viewModel.onIconProjectsClick()
            ActionIcon.ICON_POMODORO -> viewModel.onIconPomodoroClick()
        }
    }

    private fun openDatePicker() {
        val calendar = Calendar.getInstance()
        var year = calendar[Calendar.YEAR]
        var month = calendar[Calendar.MONTH]
        var day = calendar[Calendar.DAY_OF_MONTH]

        viewModel.task.date?.let {
            year = TimeHelper.getCurrentYearFromMillis(it)
            month = TimeHelper.getCurrentMonthFromMillis(it)
            day = TimeHelper.getCurrentDayFromMillis(it)
        }

        val datePickerDialog = DatePickerDialog(
            requireContext(), R.style.DatePicker, this, year, month, day
        )

        datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis

        datePickerDialog.show()
    }

    private fun openTimePicker() {
        val calendar = Calendar.getInstance()
        var hours = calendar[Calendar.HOUR_OF_DAY]
        var minutes = 0

        if (isTaskTimeTypeStart && viewModel.task.startTimeInMinutes != null) {
            hours = viewModel.task.startTimeInMinutes!! / 60
            minutes = viewModel.task.startTimeInMinutes!! % 60
        }

        if (!isTaskTimeTypeStart && viewModel.task.endTimeInMinutes != null) {
            hours = viewModel.task.endTimeInMinutes!! / 60
            minutes = viewModel.task.endTimeInMinutes!! % 60
        }

        val timePickerDialog = TimePickerDialog(
            requireContext(), R.style.DatePicker, this,
            hours, minutes, TimeHelper.isSystem24Hour
        )

        timePickerDialog.show()
    }

    private fun onSubtasksNumberChanged(number: Int) {
        viewModel.onSubtasksNumberChanged(number)
    }

    private fun onCompletedSubtasksNumberChanged(number: Int) {
        viewModel.onCompletedSubtasksNumberChanged(number)
    }

    private fun showActionsIcons(icons: ActionIcons) {
        val listItem: ListItem = generator.createActionsIconsListItem(icons)

        if (binding?.editTasksList?.isComputingLayout == false) {
            adapter.setActionsIconsListItem(listItem)
            return
        }

        binding?.editTasksList?.post {
            adapter.setActionsIconsListItem(listItem)
        }
    }

    private fun showAddSubtaskButton() {
        val listItem: ListItem = generator.createAddSubtaskButton()

        if (binding?.editTasksList?.isComputingLayout == false) {
            adapter.setAddSubtaskButtonListItem(listItem)
            return
        }

        binding?.editTasksList?.post {
            adapter.setAddSubtaskButtonListItem(listItem)
        }
    }

    private fun showSubtasks(subtasks: TasksData) {
        val listItem: ListItem = generator.createSubtasksListItems(subtasks)

        if (binding?.editTasksList?.isComputingLayout == false) {
            adapter.setSubtasksListItem(listItem)
            return
        }

        binding?.editTasksList?.post {
            adapter.setSubtasksListItem(listItem)
        }
    }

    private fun showTaskTitle(task: Task) {
        val listItem: ListItem = generator.createTitleListItem(task.taskTitle)

        if (binding?.editTasksList?.isComputingLayout == false) {
            adapter.setTitleItem(listItem)
            return
        }

        binding?.editTasksList?.post {
            adapter.setTitleItem(listItem)
        }
    }

    private fun showActionsHeader(headerActions: HeaderActions) {
        val listItem: ListItem = generator.createActionsHeaderListItem(headerActions)

        if (binding?.editTasksList?.isComputingLayout == false) {
            adapter.setActionsHeaderItem(listItem)
            return
        }

        binding?.editTasksList?.post {
            adapter.setActionsHeaderItem(listItem)
        }
    }

    private fun showDescription(task: Task) {
        setDescription(task)
    }

    private fun setDescription(task: Task) {
        val listItem: ListItem = generator.createTaskDescriptionListItem(task.description)

        if (binding?.editTasksList?.isComputingLayout == false) {
            adapter.setDescriptionItem(listItem)
            return
        }

        binding?.editTasksList?.post {
            adapter.setDescriptionItem(listItem)
        }
    }

    private fun initAdapter() {
        adapter = EditTaskAdapter(
            viewModel = viewModel,
            layoutInflater = layoutInflater,
            textInputListener = this,
            onTaskClickListener = this,
            onAddSubtaskListener = this,
            actionIconClickListener = this,
            addProjectActionsClickListener = this
        )

        listExtension = ListExtension(binding?.editTasksList)
        listExtension?.setVerticalLayoutManager()
        listExtension?.setAdapter(adapter)
    }

    private fun initObservers() {
        viewModel.taskActionsHeader.observe(viewLifecycleOwner) { headerActions ->
            showActionsHeader(headerActions)
        }

        viewModel.taskDescription.observe(viewLifecycleOwner) { task ->
            showDescription(task)
        }

        viewModel.taskTitle.observe(viewLifecycleOwner) { task ->
            showTaskTitle(task)
        }

        viewModel.actionsIcons.observe(viewLifecycleOwner) { icons ->
            showActionsIcons(icons)
        }

        viewModel.subtasks.observe(viewLifecycleOwner) { subtasks ->
            showSubtasks(subtasks)
            showAddSubtaskButton()
            onSubtasksNumberChanged(subtasks.tasks.size)
            onCompletedSubtasksNumberChanged(subtasks.tasks.count { it.completed })
        }
    }

    private fun initResultListeners() {
        setFragmentResultListener(ExtraCodes.IGNORE_CLICKED_REQUEST) { _, _ ->
            viewModel.saveTask()
        }

        setFragmentResultListener(ExtraCodes.PRIORITY_REQUEST) { requestKey, bundle ->
            viewModel.onPriorityChanged(bundle.getInt(requestKey))
        }

        setFragmentResultListener(ExtraCodes.PROJECT_REQUEST) { requestKey, bundle ->
            viewModel.onProjectChanged(bundle.getInt(requestKey))
        }

        setFragmentResultListener(ExtraCodes.POMODORO_REQUEST) { _, bundle ->
            var timeStart: Int? = bundle.getInt(ExtraCodes.POMODORO_START_TIME)
            if (timeStart == -1) timeStart = null
            viewModel.onTaskStartTimeChanged(timeStart)

            var timeEnd: Int? = bundle.getInt(ExtraCodes.POMODORO_END_TIME)
            if (timeEnd == -1) timeEnd = null
            viewModel.onTaskEndTimeChanged(timeEnd)

            var pomodoros: Int? = bundle.getInt(ExtraCodes.POMODORO_POMODOROS)
            if (pomodoros == -1) pomodoros = null
            viewModel.onPomodorosNumberChanged(pomodoros)
        }
    }

    private fun initEventsListener() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        viewModel.tasksEvent.collect { event ->
            when (event) {

                is TasksEvent.NavigateBack -> {
                    findNavController().popBackStack()
                }

                is TasksEvent.NavigateToAddTaskScreen -> {
                    val action = EditTaskFragmentDirections.actionAddEditTaskSelf(
                        title = ResourcesUtils.getString(R.string.title_add_task),
                        projectId = viewModel.task.projectId,
                        parentTaskId = viewModel.task.taskId
                    )
                    navigateTo(action)
                }

                is TasksEvent.NavigateToEditTaskScreen -> {
                    val action = EditTaskFragmentDirections.actionAddEditTaskSelf(
                        task = event.task,
                        title = ResourcesUtils.getString(R.string.title_edit_task),
                        projectId = event.task.projectId,
                        parentTaskId = event.task.taskId
                    )
                    navigateTo(action)
                }

                is TasksEvent.ShowAnalyticMessageDialog -> {
                    val action =
                        EditTaskFragmentDirections.actionAddEditTaskToAnalyticsMessagesDialog(
                            messages = event.messages
                        )
                    navigateTo(action)
                }

                is TasksEvent.NavigateToPriorityDialog -> {
                    val action = EditTaskFragmentDirections.actionAddEditTaskToPriority(
                        // todo (method for analytics which returns what technique we need to use for the display of priority)
                        // ActiveAnalytics.getPrincipleIdForPriorityDisplay
                        selectedPriorityId = event.priorityId,
                    )
                    navigateTo(action)
                }

                is TasksEvent.ShowDatePicker -> {
                    openDatePicker()
                }

                is TasksEvent.ShowTimeStartPicker -> {
                    isTaskTimeTypeStart = true
                    openTimePicker()
                }

                is TasksEvent.ShowTimeEndPicker -> {
                    isTaskTimeTypeStart = false
                    openTimePicker()
                }

                is TasksEvent.NavigateToProjectsDialog -> {
                    val action = EditTaskFragmentDirections.actionEditTaskToProjectsDialog(
                        selectedProjectId = event.projectId
                    )
                    navigateTo(action)
                }

                is TasksEvent.ShowError -> {
                    Snackbar.make(requireView(), event.error, Snackbar.LENGTH_LONG).show()
                }

                is TasksEvent.NavigateToPomodoroDialog -> {
                    val action = EditTaskFragmentDirections.actionEditTaskToPomodorosDialog(
                        currentPomodoros = event.pomodoros ?: -1,
                        currentStartTime = event.startTime ?: -1
                    )
                    navigateTo(action)
                }
            }
        }
    }
}
