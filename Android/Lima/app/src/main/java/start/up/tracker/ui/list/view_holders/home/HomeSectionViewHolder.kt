package start.up.tracker.ui.list.view_holders.home

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.HomeSectionItemBinding
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.home.HomeSection
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder

class HomeSectionViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup
) : BaseViewHolder(layoutInflater, parent, R.layout.home_section_item) {

    private val binding = HomeSectionItemBinding.bind(itemView)

    private lateinit var data: HomeSection
    private lateinit var listItem: ListItem
    private lateinit var listener: OnHomeSectionClickListener

    fun bind(listItem: ListItem, listener: OnHomeSectionClickListener) {
        this.listItem = listItem
        this.data = listItem.data as HomeSection
        this.listener = listener

        setupListener()
        setupData()
    }

    private fun setupData() {
        binding.sectionImage.setImageResource(data.iconRes)
        binding.sectionTitleText.text = data.title
        binding.tasksInsideNumberText.text = data.numberOfTasksInside.toString()
    }

    private fun setupListener() {
        binding.root.setOnClickListener {
            listener.onHomeSectionClick(listItem)
        }
    }

    interface OnHomeSectionClickListener {
        fun onHomeSectionClick(listItem: ListItem)
    }
}
