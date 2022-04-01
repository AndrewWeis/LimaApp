package start.up.tracker.ui.list.adapters.edit_task

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.list.adapters.base.BaseAdapter
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.ui.list.view_holders.edit_task.ChipViewHolder
import start.up.tracker.ui.list.view_holders.edit_task.ChipViewHolder.ChipViewHolderListener

/**
 * Adapter для отображения списка "chip"
 */
class ChipsAdapter(
    layoutInflater: LayoutInflater,
    private val chipViewHolderListener: ChipViewHolderListener,
) : BaseAdapter<ListItem, BaseViewHolder>(layoutInflater) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder =
        ChipViewHolder(layoutInflater, parent)

    override fun onBindViewHolder(holder: BaseViewHolder, item: ListItem) {
        (holder as ChipViewHolder).bind(item, chipViewHolderListener)
    }
}
