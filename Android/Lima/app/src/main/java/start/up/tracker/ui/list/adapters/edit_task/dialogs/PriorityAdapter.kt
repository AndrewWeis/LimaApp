package start.up.tracker.ui.list.adapters.edit_task.dialogs

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.list.adapters.base.BaseAdapter
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.ui.list.view_holders.edit_task.dialogs.PriorityViewHolder

class PriorityAdapter(
    layoutInflater: LayoutInflater,
    private val priorityClickListener: PriorityViewHolder.PriorityClickListener,
) : BaseAdapter<ListItem, BaseViewHolder>(layoutInflater) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriorityViewHolder =
        PriorityViewHolder(layoutInflater, parent)

    override fun onBindViewHolder(holder: BaseViewHolder, item: ListItem) {
        (holder as PriorityViewHolder).bind(item, priorityClickListener)
    }
}
