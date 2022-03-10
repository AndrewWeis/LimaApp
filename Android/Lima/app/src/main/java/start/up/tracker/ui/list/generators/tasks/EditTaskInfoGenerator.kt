package start.up.tracker.ui.list.generators.tasks

import android.text.InputType
import android.view.inputmethod.EditorInfo
import start.up.tracker.R
import start.up.tracker.entities.Task
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.Header
import start.up.tracker.ui.data.entities.forms.ListItem
import start.up.tracker.ui.data.entities.forms.ListItemTypes
import start.up.tracker.ui.data.entities.forms.Settings
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

        list.add(getHeader(ListItemIds.TASK_DATA_HEADER, R.string.title_task_data))
        list.add(getTaskTitle(task.title))
        list.add(getTaskDescription(task.description))

        list.add(getHeader(ListItemIds.TASK_PRIORITY_HEADER, R.string.title_task_priority))

        return list
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
     * Получить listItem с заголовком задачи
     *
     * @param title заголовок задачи
     * @return listItem с заголовком задачи
     */
    private fun getTaskTitle(title: String): ListItem {
        val settings = Settings(
            inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES,
            imeOption = EditorInfo.IME_ACTION_NEXT,
            hint = getHint(),
            name = ResourcesUtils.getString(R.string.task_title),
        )

        return createListItem(
            id = ListItemIds.TASK_TITLE,
            type = ListItemTypes.INPUT_TEXT,
            data = title,
            settings = settings
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
            ListItemIds.TASK_DESCRIPTION,
            ListItemTypes.INPUT_TEXT,
            description,
            settings
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
     * @param data     значение
     * @param settings  настройки
     * @return элемент списка
     */
    private fun createListItem(id: String, type: ListItemTypes, data: Any, settings: Settings): ListItem {
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
    private fun createListItem(id: String, type: ListItemTypes, data: Any): ListItem {
        return ListItem(
            id = id,
            type = type,
            data = data
        )
    }
}
