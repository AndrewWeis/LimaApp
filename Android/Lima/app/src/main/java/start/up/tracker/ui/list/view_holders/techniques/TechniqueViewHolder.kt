package start.up.tracker.ui.list.view_holders.techniques

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.TechniqueItemBinding
import start.up.tracker.entities.Technique
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder

class TechniqueViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup,
) : BaseViewHolder(layoutInflater, parent, R.layout.technique_item) {

    private var binding: TechniqueItemBinding = TechniqueItemBinding.bind(itemView)

    private lateinit var technique: Technique
    private lateinit var listener: OnTechniqueClickListener

    fun bind(listItem: ListItem, listener: OnTechniqueClickListener) {
        this.technique = listItem.data as Technique
        this.listener = listener

        setTechniqueData()
        setTechniqueClickListener()
    }

    private fun setTechniqueData() {
        binding.apply {
            techniqueTitleText.text = technique.title
            techniqueReadTimeText.text = technique.timeToRead
        }
    }

    private fun setTechniqueClickListener() {
        binding.root.setOnClickListener {
            listener.onTechniqueClick(technique)
        }
    }

    interface OnTechniqueClickListener {
        fun onTechniqueClick(technique: Technique)
    }
}
