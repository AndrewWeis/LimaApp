package start.up.tracker.ui.list.generators.eisenhower_matrix

import start.up.tracker.R
import start.up.tracker.entities.Task
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.Settings
import start.up.tracker.ui.data.entities.tasks.ChoiceData
import start.up.tracker.utils.resources.ResourcesUtils

class EisenhowerMatrixDialogGenerator {

    fun getEisenhowerPrioritiesListItems(itemId: Int): List<ListItem> {
        val listItems: MutableList<ListItem> = mutableListOf()
        listItems.add(createNoneListItem(itemId))
        listItems.add(createUrgentImportantListItem(itemId))
        listItems.add(createImportantNotUrgentListItem(itemId))
        listItems.add(createNotImportantUrgentListItem(itemId))
        listItems.add(createNotImportantNotUrgentListItem(itemId))
        return listItems
    }

    private fun createNoneListItem(itemId: Int): ListItem {
        val settings = Settings()

        val data = ChoiceData(
            id = Task.NONE,
            title = ResourcesUtils.getString(R.string.none),
            isSelected = Task.NONE == itemId
        )

        return ListItem(data = data, settings = settings)
    }

    private fun createUrgentImportantListItem(itemId: Int): ListItem {
        val settings = Settings()

        val data = ChoiceData(
            id = Task.IMPORTANT_URGENT,
            title = ResourcesUtils.getString(R.string.important_urgent),
            isSelected = Task.IMPORTANT_URGENT == itemId
        )

        return ListItem(data = data, settings = settings)
    }

    private fun createImportantNotUrgentListItem(itemId: Int): ListItem {
        val settings = Settings()

        val data = ChoiceData(
            id = Task.IMPORTANT_NOT_URGENT,
            title = ResourcesUtils.getString(R.string.important_not_urgent),
            isSelected = Task.IMPORTANT_NOT_URGENT == itemId
        )

        return ListItem(data = data, settings = settings)
    }

    private fun createNotImportantUrgentListItem(priorityId: Int): ListItem {
        val settings = Settings()

        val data = ChoiceData(
            id = Task.NOT_IMPORTANT_URGENT,
            title = ResourcesUtils.getString(R.string.not_important_urgent),
            isSelected = Task.NOT_IMPORTANT_URGENT == priorityId
        )

        return ListItem(data = data, settings = settings)
    }

    private fun createNotImportantNotUrgentListItem(itemId: Int): ListItem {
        val settings = Settings()

        val data = ChoiceData(
            id = Task.NOT_IMPORTANT_NOT_URGENT,
            title = ResourcesUtils.getString(R.string.not_important_not_urgent),
            isSelected = Task.NOT_IMPORTANT_NOT_URGENT == itemId
        )

        return ListItem(data = data, settings = settings)
    }
}
