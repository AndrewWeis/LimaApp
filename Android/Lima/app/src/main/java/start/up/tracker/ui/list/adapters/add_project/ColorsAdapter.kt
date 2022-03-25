package start.up.tracker.ui.list.adapters.add_project

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.list.adapters.base.BaseAdapter
import start.up.tracker.ui.list.view_holders.add_project.ColorViewHolder
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder

class ColorsAdapter(
    layoutInflater: LayoutInflater,
    private val colorClickListener: ColorViewHolder.ColorClickListener
) : BaseAdapter<ListItem, BaseViewHolder>(layoutInflater) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder =
        ColorViewHolder(layoutInflater, parent)

    override fun onBindViewHolder(holder: BaseViewHolder, item: ListItem) {
        (holder as ColorViewHolder).bind(item, colorClickListener)
    }
}
