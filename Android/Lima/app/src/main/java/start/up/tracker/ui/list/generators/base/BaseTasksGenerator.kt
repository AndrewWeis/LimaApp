package start.up.tracker.ui.list.generators.base

import start.up.tracker.entities.Task
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.ListItemTypes

open class BaseTasksGenerator : BaseGenerator() {

    /**
     * Получить listItem с расширенной задачей
     *
     * @param task задача
     * @return listItem с расширенной задачей
     */
    protected fun getExtendedTaskListItem(task: Task): ListItem {
        return createListItem(
            id = ListItemIds.EXTENDED_TASK,
            type = ListItemTypes.TASK,
            data = task
        )
    }

    /**
     * Получить listItem с задачей
     *
     * @param task задача
     * @return listItem с задачей
     */
    protected fun getTaskListItem(task: Task): ListItem {
        return createListItem(
            id = ListItemIds.TASK,
            type = ListItemTypes.TASK,
            data = task
        )
    }
}
