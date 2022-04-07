package start.up.tracker.ui.list.adapters.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.list.adapters.base.BaseAdapter
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.ui.list.view_holders.calendar.CalendarTimeViewHolder

class CalendarTimesAdapter(
    layoutInflater: LayoutInflater,
) : BaseAdapter<ListItem, BaseViewHolder>(layoutInflater) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarTimeViewHolder =
        CalendarTimeViewHolder(layoutInflater, parent)

    override fun onBindViewHolder(holder: BaseViewHolder, item: ListItem) {
        (holder as CalendarTimeViewHolder).bind(item)
    }
}
