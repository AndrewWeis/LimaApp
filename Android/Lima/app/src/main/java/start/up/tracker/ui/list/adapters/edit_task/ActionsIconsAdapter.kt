package start.up.tracker.ui.list.adapters.edit_task

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.list.adapters.base.BaseAdapter
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.ui.list.view_holders.edit_task.ActionIconViewHolder

class ActionsIconsAdapter(
    layoutInflater: LayoutInflater,
    private val actionIconClickListener: ActionIconViewHolder.ActionIconClickListener
) : BaseAdapter<ListItem, BaseViewHolder>(layoutInflater) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionIconViewHolder =
        ActionIconViewHolder(layoutInflater, parent)

    override fun onBindViewHolder(holder: BaseViewHolder, item: ListItem) {
        (holder as ActionIconViewHolder).bind(item, actionIconClickListener)
    }
}
