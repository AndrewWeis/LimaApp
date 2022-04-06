package start.up.tracker.ui.list.adapters.edit_task.dialogs

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.list.adapters.base.BaseAdapter
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.ui.list.view_holders.edit_task.dialogs.DialogChoiceViewHolder

class DialogChoiceAdapter(
    layoutInflater: LayoutInflater,
    private val dialogChoiceClickListener: DialogChoiceViewHolder.DialogChoiceClickListener,
) : BaseAdapter<ListItem, BaseViewHolder>(layoutInflater) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogChoiceViewHolder =
        DialogChoiceViewHolder(layoutInflater, parent)

    override fun onBindViewHolder(holder: BaseViewHolder, item: ListItem) {
        (holder as DialogChoiceViewHolder).bind(item, dialogChoiceClickListener)
    }
}
