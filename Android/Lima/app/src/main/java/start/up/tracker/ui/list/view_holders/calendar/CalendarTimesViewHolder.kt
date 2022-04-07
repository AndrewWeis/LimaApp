package start.up.tracker.ui.list.view_holders.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.CalendarTimesItemBinding
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.calendar.CalendarTimesData
import start.up.tracker.ui.extensions.list.ListExtension
import start.up.tracker.ui.list.adapters.calendar.CalendarTimesAdapter
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder

class CalendarTimesViewHolder(
    private val layoutInflater: LayoutInflater,
    parent: ViewGroup
) : BaseViewHolder(layoutInflater, parent, R.layout.calendar_times_item) {

    private val binding = CalendarTimesItemBinding.bind(itemView)

    private lateinit var data: CalendarTimesData

    private lateinit var adapter: CalendarTimesAdapter
    private var listExtension: ListExtension? = null

    init {
        initAdapter()
    }

    fun bind(listItem: ListItem) {
        this.data = listItem.data as CalendarTimesData

        updateItems()
    }

    private fun updateItems() {
        val timesListItems = data.values.map {
            val item = ListItem(data = it)
            item
        }
        adapter.updateItems(timesListItems)
    }

    private fun initAdapter() {
        adapter = CalendarTimesAdapter(
            layoutInflater = layoutInflater,
        )

        listExtension = ListExtension(binding.calendarTimesList)
        listExtension?.setVerticalLayoutManager()
        listExtension?.setAdapter(adapter)
    }
}
