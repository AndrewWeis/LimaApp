package start.up.tracker.ui.list.adapters.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.list.adapters.base.BaseAdapter
import start.up.tracker.ui.list.diff_utils.tasks.TaskDiffUtils
import start.up.tracker.ui.list.view_holders.OnTaskClickListener
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.ui.list.view_holders.tasks.CalendarTaskViewHolder

class CalendarTasksAdapter(
    layoutInflater: LayoutInflater,
    private val listener: OnTaskClickListener
) : BaseAdapter<ListItem, BaseViewHolder>(layoutInflater) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            TYPE_CALENDAR_TASK, TYPE_CALENDAR_SUBTASK ->
                CalendarTaskViewHolder(layoutInflater, parent)
            else -> throwUnknownViewHolderTypeException()
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, item: ListItem) {
        val position = holder.adapterPosition
        val nextItem: ListItem? = getNextItem(position)

        when (holder.itemViewType) {
            TYPE_CALENDAR_TASK, TYPE_CALENDAR_SUBTASK ->
                (holder as CalendarTaskViewHolder).bind(item, nextItem, position, listener)
        }
    }

    private fun getNextItem(position: Int): ListItem? {
        return if (position + 1 < itemCount) {
            getItem(position + 1)
        } else {
            null
        }
    }

    override fun getItemViewType(item: ListItem): Int {
        return when (item.id) {
            ListItemIds.CALENDAR_TASK -> TYPE_CALENDAR_TASK
            ListItemIds.CALENDAR_SUBTASK -> TYPE_CALENDAR_SUBTASK
            else -> NOT_FOUND
        }
    }

    override fun updateItems(items: List<ListItem>) {
        val diffUtils = TaskDiffUtils(getItems(), items)
        val diffResult = DiffUtil.calculateDiff(diffUtils)
        getItems().clear()
        getItems().addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    fun addListItems(listItems: List<ListItem>) {
        updateItems(listItems)
    }

    private companion object {
        const val TYPE_CALENDAR_TASK = 0
        const val TYPE_CALENDAR_SUBTASK = 1
    }
}
