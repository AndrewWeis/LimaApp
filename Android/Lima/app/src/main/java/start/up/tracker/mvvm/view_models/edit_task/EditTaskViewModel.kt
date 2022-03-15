package start.up.tracker.mvvm.view_models.edit_task

import androidx.hilt.Assisted
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import start.up.tracker.R
import start.up.tracker.data.fields.Field
import start.up.tracker.data.fields.task.EditTaskInfoFieldSet
import start.up.tracker.database.dao.CategoriesDao
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.entities.Category
import start.up.tracker.entities.Task
import start.up.tracker.ui.data.constants.ADD_RESULT_OK
import start.up.tracker.ui.data.constants.EDIT_RESULT_OK
import start.up.tracker.ui.data.entities.chips.ChipData
import start.up.tracker.ui.data.entities.chips.ChipsData
import start.up.tracker.utils.resources.ResourcesUtils
import start.up.tracker.utils.screens.StateHandleKeys
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val taskDao: TaskDao,
    categoriesDao: CategoriesDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private var isEditMode = true

    private var task = state.get<Task>(StateHandleKeys.TASK) ?: Task()
    private val selectedCategoryId = state.getLiveData<Int>(StateHandleKeys.CATEGORY_ID)

    private val _taskInfoLiveData: MutableLiveData<Task> = MutableLiveData()
    val taskInfoLiveData: LiveData<Task>
        get() = _taskInfoLiveData

    private val _titleField: MutableLiveData<Field<String>> = MutableLiveData()
    val titleField: LiveData<Field<String>>
        get() = _titleField

    private val categoriesFlow = categoriesDao.getCategories()
    private val categoriesChipsFlow: Flow<ChipsData> = combine(
        selectedCategoryId.asFlow(),
        categoriesFlow,
        ::mergeCategoriesFlows
    )
    val categoriesChips = categoriesChipsFlow.asLiveData()

    private val _prioritiesChips: MutableLiveData<ChipsData> = MutableLiveData()
    val prioritiesChips: LiveData<ChipsData>
        get() = _prioritiesChips

    private val editTaskEventChannel = Channel<AddEditTaskEvent>()
    val editTaskEvent = editTaskEventChannel.receiveAsFlow()

    private val fieldSet: EditTaskInfoFieldSet = EditTaskInfoFieldSet(task)

    init {
        if (task.title.isEmpty()) {
            isEditMode = false
        }

        showFields()
    }

    fun onSaveClick() {
        validateTitleField()

        if (!fieldSet.getTitleField().isValid()) {
            return
        }

        if (isEditMode) {
            updatedTask(task)
        } else {
            createTask(task)
        }
    }

    /**
     * Закончилось редактирование данных о задаче
     */
    fun onFinishedEditingTask() {
        showFields()
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
     * Категория задачи была изменена
     *
     * @param chipData содержащая выбранную категорию
     */
    fun onCategoryChipChanged(chipData: ChipData) {
        task = task.copy(categoryId = chipData.id)
        selectedCategoryId.postValue(chipData.id)
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

    private fun showFields() {
        showEditableTaskInfo()
        showTitleField()
        showPrioritiesChips()
        showCategoriesChips()
    }

    private fun showCategoriesChips() {
        selectedCategoryId.postValue(task.categoryId)
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

        chips.add(getChipData(PRIORITY_UNDEFINED, ResourcesUtils.getString(R.string.priority_undefined)))
        chips.add(getChipData(PRIORITY_HIGH, ResourcesUtils.getString(R.string.priority_high)))
        chips.add(getChipData(PRIORITY_MEDIUM, ResourcesUtils.getString(R.string.priority_medium)))
        chips.add(getChipData(PRIORITY_LOW, ResourcesUtils.getString(R.string.priority_low)))

        _prioritiesChips.postValue(ChipsData(values = chips))
    }

    private fun getChipData(id: Int, name: String): ChipData {
        var isSelected = false
        if (task.priority == id) {
            isSelected = true
        }

        return ChipData(
            id = id,
            name = name,
            isSelected = isSelected
        )
    }

    private fun validateTitleField() {
        fieldSet.getTitleField().validate()

        if (fieldSet.getTitleField().isValid()) {
            task = task.copy(title = fieldSet.getTitleField().getValue()!!)
        }

        showTitleField()
    }

    /**
     * Соединяет flow категорий, полученних их базы данных и flow идентификатора выбранной категории
     *
     * @param selectedCategoryId идентификатор выбранной категории
     * @param categories список категорий
     * @return chipsData с информацией о выбранной категории
     */
    private fun mergeCategoriesFlows(
        selectedCategoryId: Int,
        categories: List<Category>
    ): ChipsData {
        val values = categories.map { category ->
            var isSelected = false
            if (category.id == selectedCategoryId) {
                isSelected = true
            }

            val chipData = ChipData(
                id = category.id,
                name = category.name,
                isSelected = isSelected
            )

            chipData
        }

        return ChipsData(
            values = values
        )
    }

    private fun createTask(task: Task) = viewModelScope.launch {
        taskDao.insertTask(task)
        editTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_RESULT_OK))
    }

    private fun updatedTask(task: Task) = viewModelScope.launch {
        taskDao.updateTask(task)
        editTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_RESULT_OK))
    }

    sealed class AddEditTaskEvent {
        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()
    }

    private companion object {
        const val PRIORITY_UNDEFINED = 0
        const val PRIORITY_HIGH = 1
        const val PRIORITY_MEDIUM = 2
        const val PRIORITY_LOW = 3
    }
}
