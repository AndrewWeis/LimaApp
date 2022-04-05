package start.up.tracker.mvvm.view_models.edit_task

import androidx.hilt.Assisted
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import start.up.tracker.R
import start.up.tracker.analytics.ActiveAnalytics
import start.up.tracker.analytics.Analytics
import start.up.tracker.database.PreferencesManager
import start.up.tracker.database.dao.ProjectsDao
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.entities.Project
import start.up.tracker.entities.Task
import start.up.tracker.mvvm.view_models.tasks.base.BaseTasksOperationsViewModel
import start.up.tracker.ui.data.entities.TasksEvent
import start.up.tracker.ui.data.entities.chips.ChipData
import start.up.tracker.ui.data.entities.chips.ChipsData
import start.up.tracker.ui.data.entities.edit_task.ActionIcon
import start.up.tracker.ui.data.entities.edit_task.ActionIcons
import start.up.tracker.ui.data.entities.tasks.TasksData
import start.up.tracker.utils.screens.StateHandleKeys
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val activeAnalytics: ActiveAnalytics,
    @Assisted private val state: SavedStateHandle,
    projectsDao: ProjectsDao,
    preferencesManager: PreferencesManager,
    analytics: Analytics,
) : BaseTasksOperationsViewModel(taskDao, preferencesManager, analytics, activeAnalytics) {

    private var isEditMode = true

    val projectId = state.get<Int>(StateHandleKeys.PROJECT_ID) ?: -1
    var task = state.get<Task>(StateHandleKeys.TASK) ?: Task(projectId = projectId)
    private val parentTaskId = state.get<Int>(StateHandleKeys.PARENT_TASK_ID) ?: -1

    private val selectedProjectId: MutableLiveData<Int> = MutableLiveData(projectId)

    private val _taskDescription: MutableLiveData<Task> = MutableLiveData()
    val taskDescription: LiveData<Task> get() = _taskDescription

    private val _taskTitle: MutableLiveData<Task> = MutableLiveData()
    val taskTitle: LiveData<Task> get() = _taskTitle

    private val projectsFlow = projectsDao.getProjects()
    private val projectsChipsFlow: Flow<ChipsData> = combine(
        selectedProjectId.asFlow(),
        projectsFlow,
        ::mergeProjectsFlows
    )
    private var _projectsChips: LiveData<ChipsData> = MutableLiveData()
    val projectsChips: LiveData<ChipsData> get() = _projectsChips

    private var subtasksFlow: Flow<TasksData> = taskDao.getSubtasksByTaskId(task.taskId)
        .transform { tasks -> emit(TasksData(tasks = tasks)) }
    private var _subtasks: LiveData<TasksData> = MutableLiveData()
    val subtasks: LiveData<TasksData> get() = _subtasks

    private val _taskActionsHeader: MutableLiveData<Boolean> = MutableLiveData(task.taskTitle.isEmpty())
    val taskActionsHeader: LiveData<Boolean> get() = _taskActionsHeader

    private val _actionsIcons: MutableLiveData<ActionIcons> = MutableLiveData()
    val actionsIcons: LiveData<ActionIcons> get() = _actionsIcons

    init {
        isAddOrEditMode()
        setParentTaskId()
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

    fun onAddSubtaskClick() {
        navigateToAddSubtask()
    }

    fun onTaskTitleChanged(title: String) {
        task = task.copy(taskTitle = title)

        if (task.taskTitle.isEmpty()) {
            _taskActionsHeader.postValue(true)
        }

        if (task.taskTitle.length == 1) {
            _taskActionsHeader.postValue(false)
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

    fun onCategoryChipChanged(chipData: ChipData) {
        task = task.copy(projectId = chipData.id)
        selectedProjectId.postValue(chipData.id)
    }

    fun onPriorityChanged(priorityId: Int) {
        task = task.copy(priority = priorityId)
        // redraw
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

    fun onBackButtonClick() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateBack)
    }

    private fun setParentTaskId() {
        task = task.copy(parentTaskId = parentTaskId)
    }

    private fun showFields() {
        showTaskTitle()
        showDescription()

        // показываем подзадачи только в режиме редактирования
        if (isEditMode) {
            showSubtasks()
        }

        // показываем проекты только если это задача = (в подзадачах не показываем)
        if (task.parentTaskId == -1) {
            showProjectsChips()
        }
    }

    private fun showSubtasks() {
        _subtasks = subtasksFlow.asLiveData()
    }

    private fun showProjectsChips() {
        _projectsChips = projectsChipsFlow.asLiveData()
        // selectedProjectId.postValue(task.projectId)
    }

    private fun showDescription() {
        _taskDescription.postValue(task)
    }

    private fun showTaskTitle() {
        _taskTitle.postValue(task)
    }

    private fun showActionIcons() {
        val icons: MutableList<ActionIcon> = mutableListOf()

        icons.add(ActionIcon(id = ActionIcon.ICON_PRIORITY, iconRes = R.drawable.ic_priority_fire_1))
        icons.add(ActionIcon(id = ActionIcon.ICON_DATE, iconRes = R.drawable.ic_calendar))
        icons.add(ActionIcon(id = ActionIcon.ICON_TIME_START, iconRes = R.drawable.ic_time))
        icons.add(ActionIcon(id = ActionIcon.ICON_TIME_END, iconRes = R.drawable.ic_time))

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

    /**
     * Соединяет flow проектов, полученних их базы данных и flow идентификатора выбранного проекта
     *
     * @param selectedProjectId идентификатор выбранного проекта
     * @param projects список проектов
     * @return chipsData с информацией о выбранном проекте
     */
    private fun mergeProjectsFlows(
        selectedProjectId: Int,
        projects: List<Project>
    ): ChipsData {
        val values = projects.map { project ->
            var isSelected = false
            if (project.projectId == selectedProjectId) {
                isSelected = true
            }

            val chipData = ChipData(
                id = project.projectId,
                name = project.projectTitle,
                isSelected = isSelected
            )

            chipData
        }

        return ChipsData(
            values = values
        )
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

    private fun navigateToAddSubtask() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToAddTaskScreen)
    }

    private fun updateSubtasksNumber(number: Int) = viewModelScope.launch {
        taskDao.updateSubtasksNumber(number, task.taskId)
    }

    private fun updateCompletedSubtasksNumber(number: Int) = viewModelScope.launch {
        taskDao.updateCompletedSubtasksNumber(number, task.taskId)
    }
}
