package start.up.tracker.ui.list.view_holders.add_project

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.AddProjectActionsItemBinding
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.utils.resources.ResourcesUtils

class AddProjectActionsViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup
) : BaseViewHolder(layoutInflater, parent, R.layout.add_project_actions_item) {

    private val binding = AddProjectActionsItemBinding.bind(itemView)

    private lateinit var listener: AddProjectActionClickListener

    fun bind(listItem: ListItem, listener: AddProjectActionClickListener) {
        this.listener = listener

        setupButton(listItem.data as Boolean)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.arrowBackImage.setOnClickListener {
            listener.onBackButtonClick()
        }

        binding.doneButton.setOnClickListener {
            listener.onDoneButtonClick()
        }
    }

    private fun setupButton(isTitleEmpty: Boolean) {
        if (isTitleEmpty) {
            binding.doneButton.isEnabled = false
            binding.doneButton.setTextColor(ResourcesUtils.getColor(R.color.black))
        } else {
            binding.doneButton.isEnabled = true
            binding.doneButton.setTextColor(ResourcesUtils.getColor(R.color.primaryDarkColor))
        }
    }

    interface AddProjectActionClickListener {
        fun onDoneButtonClick()
        fun onBackButtonClick()
    }
}
