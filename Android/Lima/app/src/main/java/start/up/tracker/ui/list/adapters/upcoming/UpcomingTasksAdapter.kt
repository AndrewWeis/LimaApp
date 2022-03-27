package start.up.tracker.ui.list.adapters.upcoming

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.list.adapters.base.BaseAdapter
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.ui.list.view_holders.headers.HeaderViewHolder
import start.up.tracker.ui.list.view_holders.tasks.OnTaskClickListener
import start.up.tracker.ui.list.view_holders.tasks.TaskViewHolder

class UpcomingTasksAdapter(
    layoutInflater: LayoutInflater,
    private val listener: OnTaskClickListener
) : BaseAdapter<ListItem, BaseViewHolder>(layoutInflater) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            TYPE_SECTION_HEADER -> HeaderViewHolder(layoutInflater, parent)
            TYPE_SUBTASK, TYPE_EXTENDED_TASK -> TaskViewHolder(layoutInflater, parent)
            else -> throwUnknownViewHolderTypeException()
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, item: ListItem) {
        when (holder.itemViewType) {
            TYPE_SECTION_HEADER -> (holder as HeaderViewHolder).bind(item)
            TYPE_SUBTASK, TYPE_EXTENDED_TASK -> (holder as TaskViewHolder).bind(item, listener)
        }
    }

    override fun getItemViewType(item: ListItem): Int {
        return when (item.id) {
            ListItemIds.SECTION_HEADER -> TYPE_SECTION_HEADER
            ListItemIds.SUBTASK -> TYPE_SUBTASK
            ListItemIds.EXTENDED_TASK -> TYPE_EXTENDED_TASK
            else -> NOT_FOUND
        }
    }

    fun addListItems(listItems: List<ListItem>) {
        updateItems(listItems)
    }

    private companion object {
        const val TYPE_SECTION_HEADER = 0
        const val TYPE_SUBTASK = 1
        const val TYPE_EXTENDED_TASK = 2
    }
}
