package start.up.tracker.ui.list.generators.upcoming

import start.up.tracker.entities.Task
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.Header
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.ListItemTypes
import start.up.tracker.ui.list.generators.base.BaseTasksGenerator
import start.up.tracker.utils.TimeHelper

class UpcomingTasksGenerator : BaseTasksGenerator() {

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

        val tasksList: MutableList<Task> = mutableListOf()
        for (i in tasks.indices) {
            tasksList.add(tasks[i])
            if (i + 1 == tasks.size || tasks[i].date != tasks[i + 1].date) {
                val date = TimeHelper.formatMillisecondToDate(
                    tasks[i].date,
                    TimeHelper.DateFormats.DD_MMMM
                )
                // date != null потому что мы получили из базы данных задачи, у которых есть дата
                list.add(getHeader(date!!))

                list.addAll(getTasks(tasksList.toList()))

                tasksList.clear()
            }
        }

        return list
    }

    /**
     * Получить список [ListItem]'ы с расширенными задачами
     *
     * @param tasks задача секции
     * @return список [ListItem]'ов с расширенными задачами
     */
    private fun getTasks(tasks: List<Task>): List<ListItem> {
        val list: MutableList<ListItem> = mutableListOf()

        tasks.forEach {
            list.add(getExtendedTaskListItem(it))
        }

        return list
    }

    /**
     * Получить listItem с заголовком
     *
     * @param title заголовок
     * @return listItem с заголовком
     */
    private fun getHeader(title: String): ListItem {
        val header = Header(
            title = title
        )
        return createListItem(
            id = ListItemIds.SECTION_HEADER,
            type = ListItemTypes.HEADER,
            data = header
        )
    }
}
