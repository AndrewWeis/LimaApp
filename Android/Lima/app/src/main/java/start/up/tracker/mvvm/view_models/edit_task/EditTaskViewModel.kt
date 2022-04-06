package start.up.tracker.mvvm.view_models.edit_task

import androidx.hilt.Assisted
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import start.up.tracker.R
import start.up.tracker.analytics.ActiveAnalytics
import start.up.tracker.analytics.Analytics
import start.up.tracker.database.PreferencesManager
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.entities.Task
import start.up.tracker.mvvm.view_models.tasks.base.BaseTasksOperationsViewModel
import start.up.tracker.ui.data.entities.TasksEvent
import start.up.tracker.ui.data.entities.edit_task.ActionIcon
import start.up.tracker.ui.data.entities.edit_task.ActionIcons
import start.up.tracker.ui.data.entities.header.HeaderActions
import start.up.tracker.ui.data.entities.tasks.TasksData
import start.up.tracker.utils.resources.ResourcesUtils
import start.up.tracker.utils.screens.StateHandleKeys
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val activeAnalytics: ActiveAnalytics,
    @Assisted private val state: SavedStateHandle,
    preferencesManager: PreferencesManager,
    analytics: Analytics,
) : BaseTasksOperationsViewModel(taskDao, preferencesManager, analytics, activeAnalytics) {

    private var isEditMode = true

    val projectId = state.get<Int>(StateHandleKeys.PROJECT_ID) ?: -1
    var task = state.get<Task>(StateHandleKeys.TASK) ?: Task(projectId = projectId)
    private val parentTaskId = state.get<Int>(StateHandleKeys.PARENT_TASK_ID) ?: -1

    private val _taskDescription: MutableLiveData<Task> = MutableLiveData()
    val taskDescription: LiveData<Task> get() = _taskDescription

    private val _taskTitle: MutableLiveData<Task> = MutableLiveData()
    val taskTitle: LiveData<Task> get() = _taskTitle

    private var subtasksFlow: Flow<TasksData> = taskDao.getSubtasksByTaskId(task.taskId)
        .transform { tasks -> emit(TasksData(tasks = tasks)) }
    private var _subtasks: LiveData<TasksData> = MutableLiveData()
    val subtasks: LiveData<TasksData> get() = _subtasks

    private lateinit var headerActions: HeaderActions
    private val _taskActionsHeader: MutableLiveData<HeaderActions> = MutableLiveData()
    val taskActionsHeader: LiveData<HeaderActions> get() = _taskActionsHeader

    private val _actionsIcons: MutableLiveData<ActionIcons> = MutableLiveData()
    val actionsIcons: LiveData<ActionIcons> get() = _actionsIcons

    init {
        isAddOrEditMode()
        setParentTaskId()
        showHeaderActions()
        showFields()
        showActionIcons()
    }

    fun saveDataAboutSubtask() {
        if (isEditMode) {
            updateSubtasksNumber(task.subtasksNumber)
            updateCompletedSubtasksNumber(task.completedSubtasksNumber)
        }
    }

    fun onSaveClick() = viewModelScope.launch {
        if (isEditMode) {
            checkPrinciplesComplianceOnEditTask()
        } else {
            checkPrinciplesComplianceOnAddTask()
        }
    }

    fun saveTask() {
        if (isEditMode) {
            updateTask()
        } else {
            createTask()
        }
    }

    fun onAddSubtaskClick() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToAddTaskScreen)
    }

    fun onTaskTitleChanged(title: String) {
        task = task.copy(taskTitle = title)

        if (task.taskTitle.isEmpty()) {
            _taskActionsHeader.postValue(headerActions.copy(isDoneEnabled = false))
        }

        if (task.taskTitle.length == 1) {
            _taskActionsHeader.postValue(headerActions.copy(isDoneEnabled = true))
        }
    }

    fun onTaskDescriptionChanged(description: String) {
        task = task.copy(description = description.trim())
    }

    fun onTaskTitleClearClick() {
        task = task.copy(taskTitle = "")
    }

    fun onTaskDescriptionClearClick() {
        task = task.copy(description = "")
    }

    fun onTaskStartTimeChanged(minutes: Int) {
        task = task.copy(startTimeInMinutes = minutes)
        // show start time
    }

    fun onTaskEndTimeChanged(minutes: Int) {
        task = task.copy(endTimeInMinutes = minutes)
        // show end time
    }

    fun onTaskDateChanged(milliseconds: Long) {
        task = task.copy(date = milliseconds)
        // show date
    }

    fun onProjectChanged(projectId: Int) {
        task = task.copy(projectId = projectId)
        // show project
    }

    fun onPriorityChanged(priorityId: Int) {
        task = task.copy(priority = priorityId)
        // show priority
    }

    fun onSubtasksNumberChanged(number: Int) {
        task = task.copy(subtasksNumber = number)
    }

    fun onCompletedSubtasksNumberChanged(number: Int) {
        task = task.copy(completedSubtasksNumber = number)
    }

    fun onIconPriorityClick() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToPriorityDialog(task.priority))
    }

    fun onIconDateClick() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.ShowDatePicker(task.date))
    }

    fun onIconTimeStartClick() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.ShowTimeStartPicker(task.startTimeInMinutes))
    }

    fun onIconTimeEndClick() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.ShowTimeEndPicker(task.endTimeInMinutes))
    }

    fun onIconProjectsClick() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToProjectsDialog(task.projectId))
    }

    fun onBackButtonClick() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateBack)
    }

    private fun setParentTaskId() {
        task = task.copy(parentTaskId = parentTaskId)
    }

    private fun showHeaderActions() {
        val title = if (isEditMode) {
            ResourcesUtils.getString(R.string.title_edit_task)
        } else {
            ResourcesUtils.getString(R.string.title_add_task)
        }

        headerActions = HeaderActions(
            title = title,
            isDoneEnabled = task.taskTitle.isNotEmpty()
        )

        _taskActionsHeader.postValue(headerActions)
    }

    private fun showFields() {
        _taskTitle.postValue(task)
        _taskDescription.postValue(task)

        // показываем подзадачи только в режиме редактирования
        if (isEditMode) {
            showSubtasks()
        }
    }

    private fun showSubtasks() {
        _subtasks = subtasksFlow.asLiveData()
    }

    private fun showActionIcons() {
        val icons: MutableList<ActionIcon> = mutableListOf()

        icons.add(ActionIcon(id = ActionIcon.ICON_PRIORITY, iconRes = R.drawable.ic_priority_fire_1))
        icons.add(ActionIcon(id = ActionIcon.ICON_DATE, iconRes = R.drawable.ic_calendar))
        icons.add(ActionIcon(id = ActionIcon.ICON_TIME_START, iconRes = R.drawable.ic_time))
        icons.add(ActionIcon(id = ActionIcon.ICON_TIME_END, iconRes = R.drawable.ic_time))

        // показываем проекты только если это задача = (в подзадачах не показываем)
        if (task.parentTaskId == -1) {
            icons.add(ActionIcon(id = ActionIcon.ICON_PROJECTS, iconRes = R.drawable.ic_project))
        }

        _actionsIcons.postValue(ActionIcons(icons = icons))
    }

    private fun isAddOrEditMode() {
        if (task.taskTitle.isEmpty()) {
            isEditMode = false
        }
    }

    private suspend fun checkPrinciplesComplianceOnEditTask() {
        val analyticsMessages = activeAnalytics.checkPrinciplesComplianceOnEditTask(task)

        if (analyticsMessages.messages.isEmpty()) {
            updateTask()
            return
        }

        tasksEventChannel.send(TasksEvent.ShowAnalyticMessageDialog(analyticsMessages))
    }

    private suspend fun checkPrinciplesComplianceOnAddTask() {
        val analyticsMessages = activeAnalytics.checkPrinciplesComplianceOnAddTask(task)

        if (analyticsMessages.messages.isEmpty()) {
            createTask()
            return
        }

        tasksEventChannel.send(TasksEvent.ShowAnalyticMessageDialog(analyticsMessages))
    }

    private fun createTask() = viewModelScope.launch {
        val maxTaskId = taskDao.getTaskMaxId() ?: 0
        val newTask = task.copy(taskId = maxTaskId + 1)

        activeAnalytics.addTask(newTask)

        taskDao.insertTask(newTask)
        tasksEventChannel.send(TasksEvent.NavigateBack)
    }

    private fun updateTask() = viewModelScope.launch {
        launch { activeAnalytics.editTask(task) }

        taskDao.updateTask(task)
        tasksEventChannel.send(TasksEvent.NavigateBack)
    }

    private fun updateSubtasksNumber(number: Int) = viewModelScope.launch {
        taskDao.updateSubtasksNumber(number, task.taskId)
    }

    private fun updateCompletedSubtasksNumber(number: Int) = viewModelScope.launch {
        taskDao.updateCompletedSubtasksNumber(number, task.taskId)
    }
}
