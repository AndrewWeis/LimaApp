package start.up.tracker.ui.list.adapters.analytics_messages

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.list.adapters.base.BaseAdapter
import start.up.tracker.ui.list.view_holders.analytics_message.AnalyticsMessageViewHolder
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder

class AnalyticsMessagesDialogAdapter(
    layoutInflater: LayoutInflater,
) : BaseAdapter<ListItem, BaseViewHolder>(layoutInflater) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnalyticsMessageViewHolder =
        AnalyticsMessageViewHolder(layoutInflater, parent)

    override fun onBindViewHolder(holder: BaseViewHolder, item: ListItem) {
        (holder as AnalyticsMessageViewHolder).bind(item)
    }
}
