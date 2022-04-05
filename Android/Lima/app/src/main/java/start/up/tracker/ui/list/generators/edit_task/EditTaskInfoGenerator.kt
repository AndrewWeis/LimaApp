package start.up.tracker.ui.list.generators.edit_task

import android.text.InputType
import android.view.inputmethod.EditorInfo
import start.up.tracker.R
import start.up.tracker.data.fields.Field
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.Error
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.ListItemTypes
import start.up.tracker.ui.data.entities.Settings
import start.up.tracker.ui.data.entities.chips.ChipsData
import start.up.tracker.ui.data.entities.edit_task.ActionIcons
import start.up.tracker.ui.data.entities.tasks.TasksData
import start.up.tracker.ui.extensions.ValidationMessages
import start.up.tracker.ui.list.generators.base.BaseGenerator
import start.up.tracker.utils.TimeHelper
import start.up.tracker.utils.resources.ResourcesUtils

class EditTaskInfoGenerator : BaseGenerator() {

    /**
     * Получить listItem с кнопкой для добавления подзадачи
     *
     * @return listItem с кнопкой для добавления подзадачи
     */
    fun createAddSubtaskButton(): ListItem {
        return createListItem(
            id = ListItemIds.SUBTASK,
            type = ListItemTypes.BUTTON,
            data = null
        )
    }

    /**
     * Получить listItem с заголовком задачи
     *
     * @param field содержит заголовок задачи
     * @return listItem с заголовком задачи
     */
    fun createTitleListItem(field: Field<String>): ListItem {
        val title = field.getValue() ?: ""

        val setting = Settings(
            inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES,
            imeOption = EditorInfo.IME_ACTION_NEXT,
            editable = field.isEditable(),
            name = ResourcesUtils.getString(R.string.task_title),
            hint = getHint()
        )

        val validationMessages = ValidationMessages(field)
        val error = Error(
            message = validationMessages.getMessage()
        )

        return ListItem(
            id = ListItemIds.TASK_TITLE,
            type = ListItemTypes.INPUT_TEXT,
            data = title,
            settings = setting,
            error = error
        )
    }

    /**
     * Получить [ListItem] со списком выбираемых проектов
     *
     * @param chips список выбираемых проектов
     * @return [ListItem] содержаший список выбираемых проектов
     */
    fun createProjectsChipsListItems(chips: ChipsData): ListItem {
        return ListItem(
            id = ListItemIds.TASK_PROJECTS,
            type = ListItemTypes.LIST,
            data = chips
        )
    }

    /**
     * Получить [ListItem] со списком подзадач
     *
     * @param subtasks список подзадач
     * @return [ListItem] содержаший список подзадач
     */
    fun createSubtasksListItems(subtasks: TasksData): ListItem {
        return ListItem(
            id = ListItemIds.TASK_SUBTASKS,
            type = ListItemTypes.LIST,
            data = subtasks
        )
    }

    /**
     * Получить listItem с временем начала задачи в минутах
     *
     * @param startTimeInMinutes минуты
     * @return listItem с временем начала задачи в минутах
     */
    fun createTaskStartTimeListItem(startTimeInMinutes: Int?): ListItem {
        val settings = Settings(
            hint = getHint(),
            name = ResourcesUtils.getString(R.string.task_time_start),
            icon = R.drawable.ic_time
        )

        return createListItem(
            id = ListItemIds.TASK_TIME_START,
            type = ListItemTypes.SELECT,
            data = getFormattedTimeStart(startTimeInMinutes),
            settings = settings
        )
    }

    /**
     * Получить listItem с временем окончания задачи в минутах
     *
     * @param endTimeInMinutes минуты
     * @return listItem с временем окончания задачи в минутах
     */
    fun createTaskEndTimeListItem(endTimeInMinutes: Int?): ListItem {
        val settings = Settings(
            hint = getHint(),
            name = ResourcesUtils.getString(R.string.task_time_end),
            icon = R.drawable.ic_time
        )

        return createListItem(
            id = ListItemIds.TASK_TIME_END,
            type = ListItemTypes.SELECT,
            data = getFormattedTimeStart(endTimeInMinutes),
            settings = settings
        )
    }

    /**
     * Получить listItem с датой задачи
     *
     * @param milliseconds миллисекунды
     * @return listItem с датой окончания задачи
     */
    fun createTaskDateListItem(milliseconds: Long?): ListItem {
        val settings = Settings(
            hint = getHint(),
            name = ResourcesUtils.getString(R.string.task_date),
            icon = R.drawable.ic_calendar
        )

        val formattedDate =
            TimeHelper.formatMillisecondToDate(milliseconds, TimeHelper.DateFormats.DD_MMMM)

        return createListItem(
            id = ListItemIds.TASK_DATE,
            type = ListItemTypes.SELECT,
            data = formattedDate,
            settings = settings
        )
    }

    /**
     * Получить отформатированное время начала/окончания задачи
     *
     * @param time время начала в минутах
     * @return отформатировання время начала/окончания задачи
     */
    private fun getFormattedTimeStart(time: Int?): String? {
        return TimeHelper.formatMinutesOfCurrentDay(time)
    }

    /**
     * Получить listItem с описанием задачи
     *
     * @param description описание задачи
     * @return listItem с описанием задачи
     */
    fun createTaskDescriptionListItem(description: String): ListItem {
        val settings = Settings(
            inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES,
            imeOption = EditorInfo.IME_ACTION_NEXT,
            hint = getHint(),
            name = ResourcesUtils.getString(R.string.task_description),
        )

        return createListItem(
            id = ListItemIds.TASK_DESCRIPTION,
            type = ListItemTypes.INPUT_TEXT,
            data = description,
            settings = settings
        )
    }

    /**
     * Получить [ListItem] со списком иконок действий
     *
     * @param icons список иконок
     * @return [ListItem] содержаший список иконок
     */
    fun createActionsIconsListItem(icons: ActionIcons): ListItem {
        return ListItem(
            id = ListItemIds.ACTIONS_ICONS,
            type = ListItemTypes.LIST,
            data = icons
        )
    }

    /**
     * Получить подсказку для поля ввода
     *
     * @return подсказка для поля ввода
     */
    private fun getHint(): String {
        return ResourcesUtils.getString(R.string.hint_not_indicated)
    }
}
