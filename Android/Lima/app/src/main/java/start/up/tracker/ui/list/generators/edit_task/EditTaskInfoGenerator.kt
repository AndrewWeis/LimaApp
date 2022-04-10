package start.up.tracker.ui.list.generators.edit_task

import android.text.InputType
import android.view.inputmethod.EditorInfo
import start.up.tracker.R
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.ListItemTypes
import start.up.tracker.ui.data.entities.Settings
import start.up.tracker.ui.data.entities.edit_task.ActionIcons
import start.up.tracker.ui.data.entities.header.HeaderActions
import start.up.tracker.ui.data.entities.tasks.TasksData
import start.up.tracker.ui.list.generators.base.BaseGenerator
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
     * @param title заголовок задачи
     * @return listItem с заголовоком задачи
     */
    fun createTitleListItem(title: String): ListItem {
        val setting = Settings(
            inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES,
            imeOption = EditorInfo.IME_ACTION_NEXT,
            name = ResourcesUtils.getString(R.string.task_title),
            hint = ResourcesUtils.getString(R.string.hint_title)
        )

        return ListItem(
            id = ListItemIds.TASK_TITLE,
            type = ListItemTypes.INPUT_TEXT,
            data = title,
            settings = setting,
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
     * Получить listItem с описанием задачи
     *
     * @param description описание задачи
     * @return listItem с описанием задачи
     */
    fun createTaskDescriptionListItem(description: String): ListItem {
        val settings = Settings(
            inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES,
            imeOption = EditorInfo.IME_ACTION_NEXT,
            hint = ResourcesUtils.getString(R.string.hint_description),
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

    fun createActionsHeaderListItem(headerActions: HeaderActions): ListItem {
        return ListItem(
            type = ListItemTypes.HEADER,
            data = headerActions
        )
    }
}
