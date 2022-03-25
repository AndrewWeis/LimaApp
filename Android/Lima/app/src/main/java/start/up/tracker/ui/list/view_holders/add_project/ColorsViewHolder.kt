package start.up.tracker.ui.list.view_holders.add_project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import start.up.tracker.R
import start.up.tracker.databinding.ColorsItemBinding
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.ListItemTypes
import start.up.tracker.ui.data.entities.add_project.ColorData
import start.up.tracker.ui.data.entities.add_project.ColorsData
import start.up.tracker.ui.extensions.list.ListExtension
import start.up.tracker.ui.list.adapters.add_project.ColorsAdapter
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder

class ColorsViewHolder(
    private val layoutInflater: LayoutInflater,
    parent: ViewGroup
) : BaseViewHolder(layoutInflater, parent, R.layout.colors_item),
    ColorViewHolder.ColorClickListener {

    private val binding = ColorsItemBinding.bind(itemView)

    private lateinit var listItem: ListItem
    private lateinit var data: ColorsData

    private lateinit var listener: ColorViewHolder.ColorClickListener
    private lateinit var adapter: ColorsAdapter
    private var listExtension: ListExtension? = null

    init {
        initAdapter()
    }

    override fun onColorClick(colorData: ColorData) {
        listener.onColorClick(colorData)
    }

    fun bind(listItem: ListItem, listener: ColorViewHolder.ColorClickListener) {
        this.listItem = listItem
        this.data = listItem.data as ColorsData
        this.listener = listener

        updateItems()
    }

    private fun updateItems() {
        val colorsListItems = data.values.map {
            val item = ListItem(
                id = listItem.id,
                type = ListItemTypes.CHIP,
                data = it
            )
            item
        }
        adapter.updateItems(colorsListItems)
    }

    private fun initAdapter() {
        adapter = ColorsAdapter(
            layoutInflater = layoutInflater,
            colorClickListener = this
        )

        listExtension = ListExtension(binding.colorsList)
        listExtension?.setGridLayoutManager(COLUMNS_NUMBER, GridLayoutManager.VERTICAL)
        listExtension?.setAdapter(adapter)
    }

    private companion object {
        const val COLUMNS_NUMBER = 5
    }
}
