package start.up.tracker.ui.list.generators.calendar

import start.up.tracker.entities.Task
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.ListItemTypes
import start.up.tracker.ui.list.generators.base.BaseGenerator

class CalendarTasksGenerator : BaseGenerator() {

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
            list.add(getCalendarTaskListItem(task))
        }

        return list
    }

    /**
     * Получить listItem с задачей
     *
     * @param task задача
     * @return listItem с задачей
     */
    private fun getCalendarTaskListItem(task: Task): ListItem {
        return createListItem(
            id = ListItemIds.CALENDAR_TASK,
            type = ListItemTypes.TASK,
            data = task
        )
    }
}
