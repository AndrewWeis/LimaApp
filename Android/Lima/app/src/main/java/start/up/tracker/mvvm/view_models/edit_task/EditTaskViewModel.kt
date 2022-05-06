package start.up.tracker.mvvm.view_models.edit_task

import android.app.NotificationManager
import androidx.core.content.ContextCompat
import androidx.hilt.Assisted
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import start.up.tracker.R
import start.up.tracker.analytics.ActiveAnalytics
import start.up.tracker.analytics.Analytics
import start.up.tracker.application.App
import start.up.tracker.database.PreferencesManager
import start.up.tracker.database.TechniquesIds
import start.up.tracker.database.dao.NotificationDao
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.database.dao.TechniquesDao
import start.up.tracker.entities.Notification
import start.up.tracker.entities.NotificationType
import start.up.tracker.entities.Task
import start.up.tracker.mvvm.view_models.tasks.base.BaseTasksOperationsViewModel
import start.up.tracker.ui.data.entities.TasksEvent
import start.up.tracker.ui.data.entities.edit_task.ActionIcon
import start.up.tracker.ui.data.entities.edit_task.ActionIcons
import start.up.tracker.ui.data.entities.header.HeaderActions
import start.up.tracker.ui.data.entities.tasks.TasksData
import start.up.tracker.utils.TimeHelper
import start.up.tracker.utils.notifications.sendNotification
import start.up.tracker.utils.resources.ResourcesUtils
import start.up.tracker.utils.screens.StateHandleKeys
import java.lang.RuntimeException
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val activeAnalytics: ActiveAnalytics,
    private val principlesDao: TechniquesDao,
    private val notificationDao: NotificationDao,
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
        val timeMessage = validateTime()
        if (timeMessage != null) {
            tasksEventChannel.send(TasksEvent.ShowError(timeMessage))
            return@launch
        }

        if (isEditMode) {
            checkPrinciplesComplianceOnEditTask()
        } else {
            checkPrinciplesComplianceOnAddTask()
        }
    }

    // 0 - off
    // 1 - day
    // 2 - week
    // 3 - fortnight
    // 4 - year (birthdays!)
    val habitStatus = 1
    fun saveTask() {
        if (habitStatus == 0) {
            if (isEditMode) {
                updateTask()
            } else {
                createTask()
            }
        } else {
            if (isEditMode) {
                updateHabit()
            } else {
                createHabit()
            }
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

    fun onTaskStartTimeChanged(minutes: Int?) {
        task = task.copy(startTimeInMinutes = minutes)
    }

    fun onTaskEndTimeChanged(minutes: Int?) {
        task = task.copy(endTimeInMinutes = minutes)
    }

    fun onTaskDateChanged(milliseconds: Long) {
        task = task.copy(date = milliseconds)
    }

    fun onProjectChanged(projectId: Int) {
        task = task.copy(projectId = projectId)
    }

    fun onPriorityChanged(priorityId: Int) {
        task = task.copy(priority = priorityId)
    }

    fun onNotificationChanged(notificationTypeId: Int) {
        val notificationType = NotificationType.getByTypeId(notificationTypeId)?:return
        val notificationManager = ContextCompat.getSystemService(
            App.context,
            NotificationManager::class.java
        ) as NotificationManager

        viewModelScope.launch {
            val notificationId = updateTaskNotification(notificationType)
            val notification = notificationDao.getNotificationById(notificationId).first()
            task = task.copy(notificationId = notificationId)
            notificationManager.sendNotification(notification, App.context)
        }
    }

    private suspend fun updateTaskNotification(type: NotificationType): Long {
        var notificationId = task.notificationId
        if (notificationId != -1L) {
            val notification = notificationDao.getNotificationById(notificationId).first()
            notificationDao.updateNotification(notification.copy(type = type))
        } else {
            val notification = Notification(type = type)
            notificationId =
                notificationDao.insertNotification(notification)
        }

        return notificationId
    }

    fun onSubtasksNumberChanged(number: Int) {
        task = task.copy(subtasksNumber = number)
    }

    fun onCompletedSubtasksNumberChanged(number: Int) {
        task = task.copy(completedSubtasksNumber = number)
    }

    fun onPomodorosNumberChanged(number: Int?) {
        task = task.copy(pomodoros = number, completedPomodoros = 0)
    }

    fun onEisenhowerMatrixItemChanged(itemId: Int) {
        task = task.copy(eisenhowerMatrix = itemId)
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

    fun onIconPomodoroClick() = viewModelScope.launch {
        tasksEventChannel.send(
            TasksEvent.NavigateToPomodoroDialog(
                task.pomodoros,
                task.startTimeInMinutes
            )
        )
    }

    fun onIconNotificationsClick() = viewModelScope.launch {
        val type = if (task.notificationId == -1L) NotificationType.NONE else notificationDao.getNotificationById(task.notificationId).first().type
        tasksEventChannel.send(
            TasksEvent.NavigateToNotificationsDialog(type)
        )
    }

    fun onIconEisenhowerMatrixClick() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToEisenhowerMatrixDialog(task.eisenhowerMatrix))
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

    private fun showActionIcons() = viewModelScope.launch {
        val icons: MutableList<ActionIcon> = mutableListOf()
        val principlesIds = principlesDao.getActiveTechniquesIds()

        icons.add(ActionIcon(id = ActionIcon.ICON_PRIORITY,
            iconRes = R.drawable.ic_priority_fire_1))
        icons.add(ActionIcon(id = ActionIcon.ICON_DATE, iconRes = R.drawable.ic_calendar))

        if (!principlesIds.contains(TechniquesIds.POMODORO)) {
            icons.add(ActionIcon(id = ActionIcon.ICON_TIME_START, iconRes = R.drawable.ic_time))
            icons.add(ActionIcon(id = ActionIcon.ICON_TIME_END, iconRes = R.drawable.ic_time))
        }

        // показываем проекты только если это задача = (в подзадачах не показываем)
        if (task.parentTaskId == -1) {
            icons.add(ActionIcon(id = ActionIcon.ICON_PROJECTS, iconRes = R.drawable.ic_project))
        }

        val pomodoroActionIcon = ActionIcon(id = ActionIcon.ICON_POMODORO, iconRes = R.drawable.ic_timer)

        if (principlesIds.contains(TechniquesIds.POMODORO)) {
            icons.add(pomodoroActionIcon)
        }

        icons.add(ActionIcon(id = ActionIcon.ICON_NOTIFICATIONS, iconRes = R.drawable.ic_notifications))

        val eisenhowerMatrixActionIcon = ActionIcon(id = ActionIcon.ICON_EISENHOWER_MATRIX, iconRes = R.drawable.ic_eisenhower_matrix)
        if (principlesIds.contains(TechniquesIds.EISENHOWER_MATRIX)) {
            icons.add(eisenhowerMatrixActionIcon)
        }

        _actionsIcons.postValue(ActionIcons(icons = icons))
    }

    private suspend fun validateTime(): String? {
        if (task.startTimeInMinutes != null && task.endTimeInMinutes == null) {
            return ResourcesUtils.getString(R.string.error_start_time)
        }

        if (task.endTimeInMinutes != null && task.startTimeInMinutes == null) {
            return ResourcesUtils.getString(R.string.error_end_time)
        }

        if (task.startTimeInMinutes == null && task.endTimeInMinutes == null) {
            return null
        }

        if (task.endTimeInMinutes!! - task.startTimeInMinutes!! < 30) {
            return ResourcesUtils.getString(R.string.error_time_duration)
        }

        val doesTimeIntersect = taskDao.doesTimeIntersect(
            startTime = task.startTimeInMinutes!!,
            endTime = task.endTimeInMinutes!!,
            today = TimeHelper.getCurrentDayInMilliseconds()
        )

        if (doesTimeIntersect) {
            return ResourcesUtils.getString(R.string.error_time_does_intersect)
        }

        return null
    }

    private fun isAddOrEditMode() {
        if (task.taskTitle.isEmpty()) {
            isEditMode = false
        }
    }

    private suspend fun checkPrinciplesComplianceOnEditTask() {
        val analyticsMessages = activeAnalytics.checkPrinciplesComplianceOnEditTask(task)

        if (analyticsMessages.messages.isEmpty()) {
            val beforeDate = taskDao.getTaskById(task.taskId)[0].date
            updateTask()
            analytics.addTaskToStatisticOnEdit(beforeDate, task.date)
            return
        }

        tasksEventChannel.send(TasksEvent.ShowAnalyticMessageDialog(analyticsMessages))
    }

    private suspend fun checkPrinciplesComplianceOnAddTask() {
        val analyticsMessages = activeAnalytics.checkPrinciplesComplianceOnAddTask(task)

        if (analyticsMessages.messages.isEmpty()) {
            if (habitStatus == 0) {
                createTask()
            } else {
                createHabit();
            }
            return
        }

        tasksEventChannel.send(TasksEvent.ShowAnalyticMessageDialog(analyticsMessages))
    }

    private fun createTask() = viewModelScope.launch {
        val maxTaskId = taskDao.getTaskMaxId() ?: 0
        val newTask = task.copy(taskId = maxTaskId + 1)

        activeAnalytics.addTask(newTask)
        analytics.addTaskToStatisticOnCreate(task.date)
        taskDao.insertTask(newTask)

        tasksEventChannel.send(TasksEvent.NavigateBack)
    }

    private fun repeatHabits() = viewModelScope.launch {
        val allHabits = taskDao.getAllHabits()

        for (habit in allHabits) {
            val calendar = Calendar.getInstance()

            if (calendar.timeInMillis > task.date!! + 24 * 60 * 60 * 1000) {
                val newTask = task.copy(date = task.date!! + task.shift, completed = false,
                    wasCompleted = false)

                activeAnalytics.editTask(newTask)
                taskDao.updateTask(newTask)
            }
        }
    }

    private fun createHabit() = viewModelScope.launch {
        val maxTaskId = taskDao.getTaskMaxId() ?: 0

        if (task.date == null) {
            throw RuntimeException("Привычка без даты не допустима!")
            // TODO ошибку надо обработать - нельзя создавать привычки без указания даты
            // TODO в то же время можно создать без указания точного времени начала и коцна!
        }

        val shift : Long = when (habitStatus) {
            1 -> {
                24L * 60 * 60 * 1000
            }
            2 -> {
                24L * 60 * 60 * 1000 * 7
            }
            3 -> {
                24L * 60 * 60 * 1000 * 14
            }
            4 -> {
                24L * 60 * 60 * 1000 * 365
            }
            else -> {0L}
        }

        val newTask = task.copy(taskId = maxTaskId + 1, shift = shift)

        activeAnalytics.addTask(newTask)
        //analytics.addTaskToStatisticOnCreate(task.date)
        taskDao.insertTask(newTask)

        tasksEventChannel.send(TasksEvent.NavigateBack)

        /*var maxTaskId = taskDao.getTaskMaxId() ?: 0
        val originalTaskId = maxTaskId
        var newTask = task.copy(taskId = maxTaskId + 1, originalTaskId = originalTaskId)

        activeAnalytics.addTask(newTask)
        analytics.addTaskToStatisticOnCreate(task.date)
        taskDao.insertTask(newTask)

        if (task.date == null) {
            throw RuntimeException("Привычка без даты не допустима!")
            // TODO ошибку надо обработать - нельзя создавать привычки без указания даты
            // TODO в то же время можно создать без указания точного времени начала и коцна!
        }

        val shift : Long = when (habitStatus) {
            1 -> {
                24L * 60 * 60 * 1000
            }
            2 -> {
                24L * 60 * 60 * 1000 * 7
            }
            3 -> {
                24L * 60 * 60 * 1000 * 14
            }
            4 -> {
                24L * 60 * 60 * 1000 * 365
            }
            else -> {0L}
        }

        for (i in 1..SAMPLES) {
            maxTaskId = taskDao.getTaskMaxId() ?: 0
            newTask = task.copy(taskId = maxTaskId + 1, originalTaskId = originalTaskId,
                date = task.date!! + shift * i
            )

            activeAnalytics.addTask(newTask)
            analytics.addTaskToStatisticOnCreate(task.date!! + shift * i)
            taskDao.insertTask(newTask)
        }

        tasksEventChannel.send(TasksEvent.NavigateBack)*/
    }

    private fun updateTask() = viewModelScope.launch {
        launch { activeAnalytics.editTask(task) }

        taskDao.updateTask(task)
        tasksEventChannel.send(TasksEvent.NavigateBack)
    }

    private fun updateHabit() = viewModelScope.launch {
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
