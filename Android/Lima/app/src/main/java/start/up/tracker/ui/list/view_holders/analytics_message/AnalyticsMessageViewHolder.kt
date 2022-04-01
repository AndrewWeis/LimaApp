package start.up.tracker.ui.list.view_holders.analytics_message

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.analytics.entities.AnalyticsMessage
import start.up.tracker.databinding.AnalyticsMessageItemBinding
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder

class AnalyticsMessageViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup,
) : BaseViewHolder(layoutInflater, parent, R.layout.analytics_message_item) {

    private val binding = AnalyticsMessageItemBinding.bind(itemView)

    fun bind(listItem: ListItem) {
        val data = listItem.data as AnalyticsMessage

        binding.messageTitleText.text = data.title
        binding.messageErrorText.text = data.error
        binding.messageHintText.text = data.hint
    }
}
