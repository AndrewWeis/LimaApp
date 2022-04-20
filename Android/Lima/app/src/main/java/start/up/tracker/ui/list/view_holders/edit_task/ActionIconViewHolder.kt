package start.up.tracker.ui.list.view_holders.edit_task

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.ActionIconItemBinding
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.edit_task.ActionIcon
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.utils.resources.ResourcesUtils

class ActionIconViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup,
) : BaseViewHolder(layoutInflater, parent, R.layout.action_icon_item) {

    private val binding = ActionIconItemBinding.bind(itemView)

    private lateinit var data: ActionIcon
    private lateinit var listener: ActionIconClickListener

    fun bind(listItem: ListItem, listener: ActionIconClickListener) {
        this.data = listItem.data as ActionIcon
        this.listener = listener

        setupIcon()
        setupListener()
    }

    private fun setupListener() {
        binding.root.setOnClickListener {
            listener.onActionClickListener(data.id)
        }
    }

    private fun setupIcon() {
        binding.actionImage.setImageDrawable(ResourcesUtils.getDrawable(data.iconRes))
        binding.actionImage.setColorFilter(ResourcesUtils.getColor(data.iconColor))
    }

    interface ActionIconClickListener {
        fun onActionClickListener(actionIconId: Int)
    }
}
