package start.up.tracker.ui.list.generators.edit_task.dialogs

import start.up.tracker.R
import start.up.tracker.entities.Task
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.tasks.ChoiceData
import start.up.tracker.utils.resources.ResourcesUtils

class RepeatsGenerator {

    fun getRepeatsListItems(priorityId: Int): List<ListItem> {
        val listItems: MutableList<ListItem> = mutableListOf()
        listItems.add(createNeverListItem(priorityId))
        listItems.add(createEveryDayListItem(priorityId))
        listItems.add(createEveryWeekListItem(priorityId))
        listItems.add(createEverySecondWeekListItem(priorityId))
        return listItems
    }

    private fun createNeverListItem(itemId: Int): ListItem {
        val data = ChoiceData(
            id = Task.NEVER,
            title = ResourcesUtils.getString(R.string.repeats_never),
            isSelected = Task.NEVER == itemId
        )

        return ListItem(data = data)
    }

    private fun createEveryDayListItem(itemId: Int): ListItem {
        val data = ChoiceData(
            id = Task.EVERY_DAY,
            title = ResourcesUtils.getString(R.string.repeats_every_day),
            isSelected = Task.EVERY_DAY == itemId
        )

        return ListItem(data = data)
    }

    private fun createEveryWeekListItem(itemId: Int): ListItem {
        val data = ChoiceData(
            id = Task.EVERY_WEEK,
            title = ResourcesUtils.getString(R.string.repeats_every_week),
            isSelected = Task.EVERY_WEEK == itemId
        )

        return ListItem(data = data)
    }

    private fun createEverySecondWeekListItem(itemId: Int): ListItem {
        val data = ChoiceData(
            id = Task.EVERY_SECOND_WEEK,
            title = ResourcesUtils.getString(R.string.repeats_every_second_week),
            isSelected = Task.EVERY_SECOND_WEEK == itemId
        )

        return ListItem(data = data)
    }
}