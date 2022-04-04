package start.up.tracker.ui.list.view_holders.edit_task.dialogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.PriorityItemBinding
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.Settings
import start.up.tracker.ui.data.entities.tasks.PriorityData
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.utils.resources.ResourcesUtils

class PriorityViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup,
) : BaseViewHolder(layoutInflater, parent, R.layout.priority_item) {

    private val binding = PriorityItemBinding.bind(itemView)

    private lateinit var data: PriorityData
    private lateinit var settings: Settings
    private lateinit var listener: PriorityClickListener

    fun bind(listItem: ListItem, priorityClickListener: PriorityClickListener) {
        this.data = listItem.data as PriorityData
        this.settings = listItem.settings
        this.listener = priorityClickListener

        setupTitleText()
        setupPriorityIcon()
        setupSelectedIcon()
        setupListeners()
    }

    private fun setupListeners() {
        binding.root.setOnClickListener {
            listener.onPriorityClick(data.priorityId)
        }
    }

    private fun setupSelectedIcon() {
        binding.selectedImage.visibility = if (data.isSelected) View.VISIBLE else View.GONE
    }

    private fun setupPriorityIcon() {
        settings.icon?.let {
            binding.priorityImage.setImageDrawable(ResourcesUtils.getDrawable(it))
        }

        settings.iconColor?.let {
            binding.priorityImage.setColorFilter(ResourcesUtils.getColor(it))
        }
    }

    private fun setupTitleText() {
        binding.priorityText.text = data.title
    }

    interface PriorityClickListener {
        fun onPriorityClick(priorityId: Int)
    }
}
