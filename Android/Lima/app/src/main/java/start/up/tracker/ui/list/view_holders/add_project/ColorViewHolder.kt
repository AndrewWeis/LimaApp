package start.up.tracker.ui.list.view_holders.add_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.ColorItemBinding
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.add_project.ColorData
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder

class ColorViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup,
) : BaseViewHolder(layoutInflater, parent, R.layout.color_item) {

    private val binding = ColorItemBinding.bind(itemView)

    private lateinit var data: ColorData
    private lateinit var listItem: ListItem
    private lateinit var listener: ColorClickListener

    fun bind(listItem: ListItem, listener: ColorClickListener) {
        this.listItem = listItem
        this.data = listItem.data as ColorData
        this.listener = listener

        setupCheckedImage()
        setupCircleColor()
        setupListeners()
    }

    private fun setupListeners() {
        binding.root.setOnClickListener {
            listener.onColorClick(data)
        }
    }

    private fun setupCircleColor() {
        binding.colorImage.setColorFilter(data.colorRes)
    }

    private fun setupCheckedImage() {
        binding.checkImage.visibility = if (data.isSelected) View.VISIBLE else View.GONE
    }

    interface ColorClickListener {
        fun onColorClick(colorData: ColorData)
    }
}
