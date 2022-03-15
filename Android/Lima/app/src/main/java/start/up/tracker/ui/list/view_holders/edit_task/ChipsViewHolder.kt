package start.up.tracker.ui.list.view_holders.edit_task

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.ChipsItemBinding
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.ListItemTypes
import start.up.tracker.ui.data.entities.chips.ChipData
import start.up.tracker.ui.data.entities.chips.ChipsData
import start.up.tracker.ui.extensions.list.ListExtension
import start.up.tracker.ui.list.adapters.edit_task.ChipsAdapter
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder

class ChipsViewHolder(
    private val layoutInflater: LayoutInflater,
    parent: ViewGroup
) : BaseViewHolder(layoutInflater, parent, R.layout.chips_item),
    ChipViewHolder.ChipViewHolderListener {

    private var binding: ChipsItemBinding = ChipsItemBinding.bind(itemView)

    private lateinit var data: ChipsData
    private lateinit var listener: CategoriesViewHolderListener
    private lateinit var adapter: ChipsAdapter
    private var listExtension: ListExtension? = null

    init {
        initAdapter()
    }

    override fun onChipClick(chipData: ChipData) {
        listener.onChipClick(chipData)
    }

    fun bind(listItem: ListItem, listener: CategoriesViewHolderListener) {
        this.data = listItem.data as ChipsData
        this.listener = listener

        updateItems(data)
    }

    private fun updateItems(data: ChipsData) {
        val chipsListItems = data.values.map {
            val item = ListItem(
                id = ListItemIds.TASK_CATEGORIES,
                type = ListItemTypes.LIST,
                data = it
            )
            item
        }

        adapter.updateItems(chipsListItems)
    }

    private fun initAdapter() {
        adapter = ChipsAdapter(
            layoutInflater = layoutInflater,
            chipViewHolderListener = this
        )

        listExtension = ListExtension(binding.list)
        listExtension?.setHorizontalLayoutManager()
        listExtension?.setAdapter(adapter)
    }

    interface CategoriesViewHolderListener {
        fun onChipClick(chipData: ChipData)
    }
}
