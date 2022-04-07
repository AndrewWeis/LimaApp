package start.up.tracker.ui.list.view_holders.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.CalendarTasksItemBinding
import start.up.tracker.entities.Task
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.calendar.CalendarTasksData
import start.up.tracker.ui.extensions.list.ListExtension
import start.up.tracker.ui.list.adapters.calendar.CalendarTasksAdapter
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.ui.list.view_holders.tasks.OnTaskClickListener

class CalendarTasksViewHolder(
    private val layoutInflater: LayoutInflater,
    parent: ViewGroup
) : BaseViewHolder(layoutInflater, parent, R.layout.calendar_tasks_item),
    OnTaskClickListener {

    private val binding = CalendarTasksItemBinding.bind(itemView)

    private lateinit var data: CalendarTasksData

    private lateinit var listener: OnTaskClickListener
    private lateinit var adapter: CalendarTasksAdapter
    private var listExtension: ListExtension? = null

    init {
        initAdapter()
    }

    override fun onTaskClick(task: Task) {
        listener.onTaskClick(task)
    }

    override fun onCheckBoxClick(task: Task) {
        listener.onCheckBoxClick(task)
    }

    fun bind(listItem: ListItem, listener: OnTaskClickListener) {
        this.data = listItem.data as CalendarTasksData
        this.listener = listener

        updateItems()
    }

    private fun updateItems() {
        val tasksListItems = data.values.map {
            ListItem(data = it)
        }
        adapter.updateItems(tasksListItems)
    }

    private fun initAdapter() {
        adapter = CalendarTasksAdapter(
            layoutInflater = layoutInflater,
            listener = this
        )

        listExtension = ListExtension(binding.calendarTasksList)
        listExtension?.setVerticalLayoutManager()
        listExtension?.setAdapter(adapter)
    }
}
