package start.up.tracker.ui.list.view_holders.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.CalendarTimeItemBinding
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder

class CalendarTimeViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup
) : BaseViewHolder(layoutInflater, parent, R.layout.calendar_time_item) {

    private val binding = CalendarTimeItemBinding.bind(itemView)

    fun bind(listItem: ListItem) {
        binding.calendarTimeText.text = listItem.data as String
    }
}
