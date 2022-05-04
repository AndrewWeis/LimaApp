package start.up.tracker.ui.list.view_holders.edit_task.dialogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.DialogChoiceItemBinding
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.Settings
import start.up.tracker.ui.data.entities.tasks.ChoiceData
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.utils.resources.ResourcesUtils

class DialogChoiceViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup,
) : BaseViewHolder(layoutInflater, parent, R.layout.dialog_choice_item) {

    private val binding = DialogChoiceItemBinding.bind(itemView)

    private lateinit var data: ChoiceData
    private lateinit var settings: Settings
    private lateinit var listener: DialogChoiceClickListener

    fun bind(listItem: ListItem, dialogChoiceClickListener: DialogChoiceClickListener) {
        this.data = listItem.data as ChoiceData
        this.settings = listItem.settings
        this.listener = dialogChoiceClickListener

        setupTitleText()
        setupChoiceIcon()
        setupSelectedIcon()
        setupListeners()
    }

    private fun setupListeners() {
        binding.root.setOnClickListener {
            listener.onChoiceClick(data.id)
        }
    }

    private fun setupSelectedIcon() {
        binding.selectedImage.visibility = if (data.isSelected) View.VISIBLE else View.GONE
    }

    private fun setupChoiceIcon() {
        settings.width?.let {
            binding.choiceImage.layoutParams.width = ResourcesUtils.getPxByDp(it.toFloat())
        }

        settings.height?.let {
            binding.choiceImage.layoutParams.height = ResourcesUtils.getPxByDp(it.toFloat())
        }

        settings.icon?.let {
            binding.choiceImage.setImageDrawable(ResourcesUtils.getDrawable(it))
        }

        settings.iconColor?.let {
            binding.choiceImage.setColorFilter(it)
        }
    }

    private fun setupTitleText() {
        binding.choiceText.text = data.title
    }

    interface DialogChoiceClickListener {
        fun onChoiceClick(choiceId: Int)
    }
}
