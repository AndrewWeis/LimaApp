package start.up.tracker.ui.list.generators.tasks

import start.up.tracker.entities.Task
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.list.generators.base.BaseTasksGenerator

class TasksGenerator : BaseTasksGenerator() {

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
}
