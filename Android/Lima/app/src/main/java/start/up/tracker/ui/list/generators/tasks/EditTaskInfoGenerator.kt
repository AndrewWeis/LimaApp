package start.up.tracker.ui.list.generators.tasks

import android.text.InputType
import android.view.inputmethod.EditorInfo
import start.up.tracker.R
import start.up.tracker.data.fields.Field
import start.up.tracker.entities.Task
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.Header
import start.up.tracker.ui.data.entities.forms.Error
import start.up.tracker.ui.data.entities.forms.ListItem
import start.up.tracker.ui.data.entities.forms.ListItemTypes
import start.up.tracker.ui.data.entities.forms.Settings
import start.up.tracker.ui.extensions.ValidationMessages
import start.up.tracker.utils.TimeHelper
import start.up.tracker.utils.resources.ResourcesUtils

class EditTaskInfoGenerator {

    /**
     * Получить список с информацией о задаче
     *
     * @param task информация о задача
     * @return спикок с информацией о задаче
     */
    fun createListItems(task: Task?): List<ListItem> {
        if (task == null) {
            return listOf()
        }

        val list: MutableList<ListItem> = mutableListOf()

        list.add(getTaskDescription(task.description))

        // list.add(getHeader(ListItemIds.TASK_PRIORITY_HEADER, R.string.title_task_priority))
        list.add(getTaskStartTime(task.startTimeInMinutes))
        list.add(getTaskEndTime(task.endTimeInMinutes))
        list.add(getTaskDate(task.date))

        return list
    }

    /**
     * Получить listItem с временем начала задачи в минутах
     *
     * @param startTimeInMinutes минуты
     * @return listItem с временем начала задачи в минутах
     */
    private fun getTaskStartTime(startTimeInMinutes: Int?): ListItem {
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
    private fun getTaskEndTime(endTimeInMinutes: Int?): ListItem {
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
    private fun getTaskDate(milliseconds: Long?): ListItem {
        val settings = Settings(
            hint = getHint(),
            name = ResourcesUtils.getString(R.string.task_date),
            icon = R.drawable.ic_calendar
        )

        val formattedDate =
            TimeHelper.formatMillisecondToDate(milliseconds, TimeHelper.DateFormats.DD_MM)

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
     * Получить заголовок
     *
     * @param stringId интификатор ресурса строки
     * @return заголовок
     */
    private fun getHeader(id: String, stringId: Int): ListItem {
        val header = Header(
            title = ResourcesUtils.getString(stringId)
        )
        return createListItem(
            id = id,
            type = ListItemTypes.HEADER,
            data = header
        )
    }

    /**
     * Получить listItem с описанием задачи
     *
     * @param description описание задачи
     * @return listItem с описанием задачи
     */
    private fun getTaskDescription(description: String): ListItem {
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
     * Получить подсказку для поля ввода
     *
     * @return подсказка для поля ввода
     */
    private fun getHint(): String {
        return ResourcesUtils.getString(R.string.hint_not_indicated)
    }

    /**
     * Создает [элемент списка][ListItem]
     *
     * @param id        идентификатор элемента
     * @param type      тип элемента
     * @param data      значение
     * @param settings  настройки
     * @return элемент списка
     */
    private fun createListItem(id: String, type: ListItemTypes, data: Any?, settings: Settings): ListItem {
        val listItem: ListItem = createListItem(id, type, data)
        return listItem.copy(settings = settings)
    }

    /**
     * Создает [элемент списка][ListItem]
     *
     * @param id        идентификатор элемента
     * @param type      тип элемента
     * @param data     значение
     * @return элемент списка
     */
    private fun createListItem(id: String, type: ListItemTypes, data: Any?): ListItem {
        return ListItem(
            id = id,
            type = type,
            data = data
        )
    }
}
