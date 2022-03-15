package start.up.tracker.ui.list.view_holders.edit_task

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.ChipItemBinding
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.chips.ChipData
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.utils.resources.ResourcesUtils

class ChipViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup,
) : BaseViewHolder(layoutInflater, parent, R.layout.chip_item) {

    private val binding: ChipItemBinding = ChipItemBinding.bind(itemView)

    private lateinit var data: ChipData
    private lateinit var listener: ChipViewHolderListener

    fun bind(listItem: ListItem, listener: ChipViewHolderListener) {
        this.data = listItem.data as ChipData
        this.listener = listener

        setupTitleText()
        setupChipView()
        setupListeners()
    }

    private fun setupListeners() {
        binding.chipView.setOnClickListener {
            if (data.isSelected) {
                return@setOnClickListener
            }

            data.isSelected = true
            listener.onChipClick(data)
        }
    }

    private fun setupTitleText() {
        binding.titleText.text = data.name
    }

    private fun setupChipView() {
        val strokeWidthInDp = if (data.isSelected) 1 else 0.5f
        binding.chipView.strokeWidth = ResourcesUtils.getPxByDp(strokeWidthInDp.toFloat())

        val strokeColorRes = if (data.isSelected) R.color.primaryColor else R.color.black
        binding.chipView.strokeColor = ResourcesUtils.getColor(strokeColorRes)

        binding.titleText.isSelected = data.isSelected
    }

    interface ChipViewHolderListener {
        fun onChipClick(chipData: ChipData)
    }
}
