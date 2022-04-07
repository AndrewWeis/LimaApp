package start.up.tracker.ui.list.adapters.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.list.adapters.base.BaseAdapter
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.ui.list.view_holders.calendar.CalendarTaskViewHolder
import start.up.tracker.ui.list.view_holders.tasks.OnTaskClickListener

class CalendarTasksAdapter(
    layoutInflater: LayoutInflater,
    private val listener: OnTaskClickListener
) : BaseAdapter<ListItem, BaseViewHolder>(layoutInflater) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarTaskViewHolder =
        CalendarTaskViewHolder(layoutInflater, parent)

    override fun onBindViewHolder(holder: BaseViewHolder, item: ListItem) {
        val position = holder.adapterPosition
        val nextItem: ListItem? = getNextItem(position)

        (holder as CalendarTaskViewHolder).bind(item, nextItem, position, listener)
    }

    private fun getNextItem(position: Int): ListItem? {
        return if (position + 1 < itemCount) {
            getItem(position + 1)
        } else {
            null
        }
    }
}
