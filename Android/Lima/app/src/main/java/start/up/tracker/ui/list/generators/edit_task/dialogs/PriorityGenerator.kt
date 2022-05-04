package start.up.tracker.ui.list.generators.edit_task.dialogs

import start.up.tracker.R
import start.up.tracker.entities.Task
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.Settings
import start.up.tracker.ui.data.entities.tasks.ChoiceData
import start.up.tracker.utils.resources.ResourcesUtils

class PriorityGenerator {

    fun getDefaultPrioritiesListItems(priorityId: Int): List<ListItem> {
        val listItems: MutableList<ListItem> = mutableListOf()
        listItems.add(createNoPriorityListItem(priorityId))
        listItems.add(createHighPriorityListItem(priorityId))
        listItems.add(createMediumPriorityListItem(priorityId))
        listItems.add(createLowPriorityListItem(priorityId))
        return listItems
    }

    private fun createNoPriorityListItem(priorityId: Int): ListItem {
        val settings = Settings(
            icon = R.drawable.ic_priority_fire_1,
            iconColor = ResourcesUtils.getColor(R.color.gray)
        )

        val data = ChoiceData(
            id = Task.NONE,
            title = ResourcesUtils.getString(R.string.priority_undefined),
            isSelected = Task.NONE == priorityId
        )

        return ListItem(data = data, settings = settings)
    }

    private fun createHighPriorityListItem(priorityId: Int): ListItem {
        val settings = Settings(
            icon = R.drawable.ic_priority_fire_1,
            iconColor = ResourcesUtils.getColor(R.color.priority_high)
        )

        val data = ChoiceData(
            id = Task.PRIORITY_HIGH,
            title = ResourcesUtils.getString(R.string.priority_high),
            isSelected = Task.PRIORITY_HIGH == priorityId
        )

        return ListItem(data = data, settings = settings)
    }

    private fun createMediumPriorityListItem(priorityId: Int): ListItem {
        val settings = Settings(
            icon = R.drawable.ic_priority_fire_1,
            iconColor = ResourcesUtils.getColor(R.color.priority_medium)
        )

        val data = ChoiceData(
            id = Task.PRIORITY_MEDIUM,
            title = ResourcesUtils.getString(R.string.priority_medium),
            isSelected = Task.PRIORITY_MEDIUM == priorityId
        )

        return ListItem(data = data, settings = settings)
    }

    private fun createLowPriorityListItem(priorityId: Int): ListItem {
        val settings = Settings(
            icon = R.drawable.ic_priority_fire_1,
            iconColor = ResourcesUtils.getColor(R.color.priority_low)
        )

        val data = ChoiceData(
            id = Task.PRIORITY_LOW,
            title = ResourcesUtils.getString(R.string.priority_low),
            isSelected = Task.PRIORITY_LOW == priorityId
        )

        return ListItem(data = data, settings = settings)
    }
}
