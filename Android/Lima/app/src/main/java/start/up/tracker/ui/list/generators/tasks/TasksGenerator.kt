package start.up.tracker.ui.list.generators.tasks

import start.up.tracker.entities.Task
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.forms.ListItem
import start.up.tracker.ui.data.entities.forms.ListItemTypes
import start.up.tracker.ui.list.generators.base.BaseGenerator

class TasksGenerator : BaseGenerator() {

    /**
     * Получить список [ListItem]'ов
     *
     * @param tasks список задач
     * @return список [ListItem]'ов
     */
    fun createListItems(tasks: List<Task>?): List<ListItem> {
        if (tasks.isNullOrEmpty()) {
            return listOf()
        }

        val list: MutableList<ListItem> = mutableListOf()

        tasks.forEach { task ->
            if (task.categoryName != null) {
                list.add(getExtendedTaskListItem(task))
            } else {
                list.add(getTaskListItem(task))
            }
        }

        return list
    }

    /**
     * Получить listItem с расширенной задачей
     *
     * @param task задача
     * @return listItem с расширенной задачей
     */
    private fun getExtendedTaskListItem(task: Task): ListItem {
        return createListItem(
            id = ListItemIds.EXTENDED_TASK,
            type = ListItemTypes.ITEM,
            data = task
        )
    }

    /**
     * Получить listItem с задачей
     *
     * @param task задача
     * @return listItem с задачей
     */
    private fun getTaskListItem(task: Task): ListItem {
        return createListItem(
            id = ListItemIds.TASK,
            type = ListItemTypes.ITEM,
            data = task
        )
    }
}
