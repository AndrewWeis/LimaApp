package start.up.tracker.ui.list.adapters.techniques

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.ListItemTypes
import start.up.tracker.ui.list.adapters.base.BaseAdapter
import start.up.tracker.ui.list.diff_utils.techniques.TechniqueDiffUtils
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.ui.list.view_holders.techniques.TechniqueViewHolder

class TechniquesAdapter(
    layoutInflater: LayoutInflater,
    private val listener: TechniqueViewHolder.OnTechniqueClickListener
) : BaseAdapter<ListItem, BaseViewHolder>(layoutInflater) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            TYPE_TECHNIQUE_ENTRY -> TechniqueViewHolder(layoutInflater, parent)
            else -> throwUnknownViewHolderTypeException()
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, item: ListItem) {
        when (holder.itemViewType) {
            TYPE_TECHNIQUE_ENTRY -> (holder as TechniqueViewHolder).bind(item, listener)
        }
    }

    override fun getItemViewType(item: ListItem): Int {
        return when (item.type) {
            ListItemTypes.TECHNIQUE -> TYPE_TECHNIQUE_ENTRY
            else -> NOT_FOUND
        }
    }

    override fun updateItems(items: List<ListItem>) {
        val diffUtils = TechniqueDiffUtils(getItems(), items)
        val diffResult = DiffUtil.calculateDiff(diffUtils)
        getItems().clear()
        getItems().addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    private companion object {
        const val TYPE_TECHNIQUE_ENTRY = 0
    }
}
