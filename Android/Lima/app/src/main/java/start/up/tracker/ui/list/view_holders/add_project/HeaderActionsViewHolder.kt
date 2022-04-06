package start.up.tracker.ui.list.view_holders.add_project

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.HeaderActionsItemBinding
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.header.HeaderActions
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.utils.resources.ResourcesUtils

class HeaderActionsViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup
) : BaseViewHolder(layoutInflater, parent, R.layout.header_actions_item) {

    private val binding = HeaderActionsItemBinding.bind(itemView)

    private lateinit var data: HeaderActions
    private lateinit var listener: AddProjectActionClickListener

    fun bind(listItem: ListItem, listener: AddProjectActionClickListener) {
        this.listener = listener
        this.data = listItem.data as HeaderActions

        setupButton()
        setupHeaderTitle()
        setupClickListeners()
    }

    private fun setupHeaderTitle() {
        binding.title.text = data.title
    }

    private fun setupClickListeners() {
        binding.arrowBackImage.setOnClickListener {
            listener.onBackButtonClick()
        }

        binding.doneButton.setOnClickListener {
            listener.onDoneButtonClick()
        }
    }

    private fun setupButton() {
        if (data.isDoneEnabled) {
            binding.doneButton.isEnabled = true
            binding.doneButton.setTextColor(ResourcesUtils.getColor(R.color.primaryDarkColor))
        } else {
            binding.doneButton.isEnabled = false
            binding.doneButton.setTextColor(ResourcesUtils.getColor(R.color.black))
        }
    }

    interface AddProjectActionClickListener {
        fun onDoneButtonClick()
        fun onBackButtonClick()
    }
}
