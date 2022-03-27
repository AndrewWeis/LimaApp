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
import start.up.tracker.data.fields.Field
import start.up.tracker.data.fields.task.EditTaskInfoFieldSet
import start.up.tracker.database.PreferencesManager
import start.up.tracker.database.dao.ProjectsDao
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.entities.Project
import start.up.tracker.entities.Task
import start.up.tracker.mvvm.view_models.tasks.base.BaseTasksOperationsViewModel
import start.up.tracker.ui.data.entities.TasksEvent
import start.up.tracker.ui.data.entities.chips.ChipData
import start.up.tracker.ui.data.entities.chips.ChipsData
import start.up.tracker.ui.data.entities.tasks.TasksData
import start.up.tracker.utils.resources.ResourcesUtils
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

    private val _taskInfoLiveData: MutableLiveData<Task> = MutableLiveData()
    val taskInfoLiveData: LiveData<Task> get() = _taskInfoLiveData

    private val _titleField: MutableLiveData<Field<String>> = MutableLiveData()
    val titleField: LiveData<Field<String>> get() = _titleField

    private val projectsFlow = projectsDao.getProjects()
    private val projectsChipsFlow: Flow<ChipsData> = combine(
        selectedProjectId.asFlow(),
        projectsFlow,
        ::mergeProjectsFlows
    )
    private var _projectsChips: LiveData<ChipsData> = MutableLiveData()
    val projectsChips: LiveData<ChipsData> get() = _projectsChips

    private val _prioritiesChips: MutableLiveData<ChipsData> = MutableLiveData()
    val prioritiesChips: LiveData<ChipsData> get() = _prioritiesChips

    private val fieldSet: EditTaskInfoFieldSet = EditTaskInfoFieldSet(task)

    private var subtasksFlow: Flow<TasksData> = taskDao.getSubtasksByTaskId(task.taskId)
        .transform { tasks -> emit(TasksData(tasks = tasks)) }
    private var _subtasks: LiveData<TasksData> = MutableLiveData()
    val subtasks: LiveData<TasksData> get() = _subtasks

    init {
        isAddOrEditMode()
        setParentTaskId()
        showFields()
    }

    fun saveDataAboutSubtask() {
        if (isEditMode) {
            updateSubtasksNumber(task.subtasksNumber)
            updateCompletedSubtasksNumber(task.completedSubtasksNumber)
        }
    }

    fun onSaveClick() {
        validateTitleField()

        if (!fieldSet.getTitleField().isValid()) {
            return
        }

        if (isEditMode) {
            updateTask()
        } else {
            createTask()
        }
    }

    /**
     * Была нажата кнопка "Добавить подзадачу"
     */
    fun onAddSubtaskClick() {
        navigateToAddSubtask()
    }

    /**
     * Заголовок задачи был изменен
     *
     * @param title заголовок
     */
    fun onTaskTitleChanged(title: String) {
        fieldSet.onTitleChange(title.trim())
    }

    /**
     * Описание задачи было изменено
     *
     * @param description описание
     */
    fun onTaskDescriptionChanged(description: String) {
        task = task.copy(description = description.trim())
    }

    /**
     * Заголовок задачи был очищен
     */
    fun onTaskTitleClearClick() {
        fieldSet.onTitleChange("")
    }

    /**
     * Описание задачи было очищено
     */
    fun onTaskDescriptionClearClick() {
        task = task.copy(description = "")
    }

    /**
     * Время начала задачи было изменено
     *
     * @param minutes минуты
     */
    fun onTaskStartTimeChanged(minutes: Int) {
        task = task.copy(startTimeInMinutes = minutes)
        showFields()
    }

    /**
     * Время окончания задачи было изменено
     *
     * @param minutes минуты
     */
    fun onTaskEndTimeChanged(minutes: Int) {
        task = task.copy(endTimeInMinutes = minutes)
        showFields()
    }

    /**
     * Дата окончания задачи была изменена
     *
     * @param milliseconds дата в миллисекунды
     */
    fun onTaskDateChanged(milliseconds: Long) {
        task = task.copy(date = milliseconds)
        showFields()
    }

    /**
     * Проект задачи была изменена
     *
     * @param chipData содержащая выбранную категорию
     */
    fun onCategoryChipChanged(chipData: ChipData) {
        task = task.copy(projectId = chipData.id)
        selectedProjectId.postValue(chipData.id)
    }

    /**
     * Приоритет задачи была изменен
     *
     * @param chipData содержащая выбранный приоритет
     */
    fun onPriorityChipChanged(chipData: ChipData) {
        task = task.copy(priority = chipData.id)
        showPrioritiesChips()
    }

    /**
     * Количество подзадач было изменено
     *
     * @param number количество подзадач
     */
    fun onSubtasksNumberChanged(number: Int) {
        task = task.copy(subtasksNumber = number)
    }

    /**
     * Количество выполненных подзадач было изменено
     *
     * @param number количество выполненных подзадач
     */
    fun onCompletedSubtasksNumberChanged(number: Int) {
        task = task.copy(completedSubtasksNumber = number)
    }

    private fun setParentTaskId() {
        task = task.copy(parentTaskId = parentTaskId)
    }

    private fun showFields() {
        showEditableTaskInfo()
        showTitleField()
        showPrioritiesChips()

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

    private fun showEditableTaskInfo() {
        _taskInfoLiveData.postValue(task)
    }

    private fun showTitleField() {
        val field: Field<String> = fieldSet.getTitleField()
        _titleField.postValue(field)
    }

    private fun showPrioritiesChips() {
        val chips: MutableList<ChipData> = mutableListOf()

        chips.add(
            getChipData(
                PRIORITY_UNDEFINED,
                ResourcesUtils.getString(R.string.priority_undefined)
            )
        )
        chips.add(getChipData(PRIORITY_HIGH, ResourcesUtils.getString(R.string.priority_high)))
        chips.add(getChipData(PRIORITY_MEDIUM, ResourcesUtils.getString(R.string.priority_medium)))
        chips.add(getChipData(PRIORITY_LOW, ResourcesUtils.getString(R.string.priority_low)))

        _prioritiesChips.postValue(ChipsData(values = chips))
    }

    private fun getChipData(id: Int, name: String) = ChipData(
        id = id,
        name = name,
        isSelected = task.priority == id
    )

    private fun validateTitleField() {
        fieldSet.getTitleField().validate()

        if (fieldSet.getTitleField().isValid()) {
            task = task.copy(taskTitle = fieldSet.getTitleField().getValue()!!)
        }

        showTitleField()
    }

    private fun isAddOrEditMode() {
        if (task.taskTitle.isEmpty()) {
            isEditMode = false
        }
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

        taskDao.insertTask(newTask)

        activeAnalytics.managerAddTask(newTask)
        activeAnalytics.addTask(newTask)

        tasksEventChannel.send(TasksEvent.NavigateBack)
    }

    private fun updateTask() = viewModelScope.launch {
        taskDao.updateTask(task)

        activeAnalytics.managerEditTask(task)
        activeAnalytics.editTask(task)

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

    private companion object {
        const val PRIORITY_UNDEFINED = 0
        const val PRIORITY_HIGH = 1
        const val PRIORITY_MEDIUM = 2
        const val PRIORITY_LOW = 3
    }
}
