package start.up.tracker.ui.list.adapters.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.list.adapters.base.BaseSequenceAdapter
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.ui.list.view_holders.calendar.CalendarTasksViewHolder
import start.up.tracker.ui.list.view_holders.calendar.CalendarTimesViewHolder
import start.up.tracker.ui.list.view_holders.tasks.OnTaskClickListener

class CalendarAdapter(
    layoutInflater: LayoutInflater,
    private val onTaskClickListener: OnTaskClickListener
) : BaseSequenceAdapter<ListItem, BaseViewHolder>(layoutInflater) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            ITEM_CALENDAR_TIMES_LIST ->
                return CalendarTimesViewHolder(layoutInflater, parent)
            ITEM_CALENDAR_TASKS_LIST ->
                return CalendarTasksViewHolder(layoutInflater, parent)
            else -> throwUnknownViewHolderTypeException()
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, item: ListItem) {
        when (holder.itemViewType) {
            ITEM_CALENDAR_TIMES_LIST ->
                return (holder as CalendarTimesViewHolder).bind(item)
            ITEM_CALENDAR_TASKS_LIST ->
                return (holder as CalendarTasksViewHolder).bind(item, onTaskClickListener)
        }
    }

    override fun getItemViewType(item: ListItem): Int {
        return when (item.id) {
            ListItemIds.CALENDAR_TIMES -> ITEM_CALENDAR_TIMES_LIST
            ListItemIds.CALENDAR_TASKS -> ITEM_CALENDAR_TASKS_LIST
            else -> NOT_FOUND
        }
    }

    override fun getTypeSequence() = intArrayOf(
        ITEM_CALENDAR_TIMES_LIST,
        ITEM_CALENDAR_TASKS_LIST
    )

    fun setTimeItem(listItem: ListItem) {
        updateItem(listItem, ITEM_CALENDAR_TIMES_LIST)
    }

    fun setTaskItem(listItem: ListItem) {
        updateItem(listItem, ITEM_CALENDAR_TASKS_LIST)
    }

    private companion object {
        const val ITEM_CALENDAR_TIMES_LIST = 0
        const val ITEM_CALENDAR_TASKS_LIST = 1
    }
}
