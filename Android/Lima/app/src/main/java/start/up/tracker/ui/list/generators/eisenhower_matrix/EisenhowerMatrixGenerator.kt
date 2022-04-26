package start.up.tracker.ui.list.generators.eisenhower_matrix

import start.up.tracker.R
import start.up.tracker.entities.Task
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.ListItemTypes
import start.up.tracker.ui.data.entities.header.Header
import start.up.tracker.ui.list.generators.base.BaseTasksGenerator
import start.up.tracker.utils.resources.ResourcesUtils

class EisenhowerMatrixGenerator : BaseTasksGenerator() {

    fun createListItems(tasks: List<Task>?): List<ListItem> {
        if (tasks.isNullOrEmpty()) {
            return listOf()
        }

        val list: MutableList<ListItem> = mutableListOf()

        var currentOption = 1
        while (currentOption != 5) {
            val header = getHeaderListItem(currentOption)
            list.add(header)

            val tasksList = tasks.filter { task ->
                task.eisenhowerMatrix == currentOption
            }
            list.addAll(getTasks(tasksList))

            currentOption++
        }

        return list
    }

    private fun getTasks(tasks: List<Task>): List<ListItem> {
        val list: MutableList<ListItem> = mutableListOf()

        tasks.forEach {
            list.add(getExtendedTaskListItem(it))
        }

        return list
    }

    private fun getHeaderListItem(optionId: Int): ListItem {
        val header = Header(
            title = getHeader(optionId)
        )
        return createListItem(
            id = ListItemIds.SECTION_HEADER,
            type = ListItemTypes.HEADER,
            data = header
        )
    }

    private fun getHeader(optionId: Int): String {
        return when (optionId) {
            Task.IMPORTANT_URGENT -> ResourcesUtils.getString(R.string.important_urgent)
            Task.IMPORTANT_NOT_URGENT -> ResourcesUtils.getString(R.string.important_not_urgent)
            Task.NOT_IMPORTANT_URGENT -> ResourcesUtils.getString(R.string.not_important_urgent)
            Task.NOT_IMPORTANT_NOT_URGENT -> ResourcesUtils.getString(R.string.not_important_not_urgent)
            else -> ""
        }
    }
}
