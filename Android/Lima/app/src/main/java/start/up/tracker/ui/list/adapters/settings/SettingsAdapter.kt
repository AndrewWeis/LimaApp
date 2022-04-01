package start.up.tracker.ui.list.adapters.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.list.adapters.base.BaseAdapter
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.ui.list.view_holders.settings.SettingsViewHolder

class SettingsAdapter(
    layoutInflater: LayoutInflater,
    private val listener: SettingsViewHolder.OnSettingClickListener
) : BaseAdapter<ListItem, BaseViewHolder>(layoutInflater) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return SettingsViewHolder(layoutInflater, parent)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, item: ListItem) {
        (holder as SettingsViewHolder).bind(item, listener)
    }
}
