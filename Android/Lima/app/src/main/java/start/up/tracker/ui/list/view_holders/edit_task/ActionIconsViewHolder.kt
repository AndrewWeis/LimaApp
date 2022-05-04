package start.up.tracker.ui.list.view_holders.edit_task

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.BaseListItemBinding
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.ListItemTypes
import start.up.tracker.ui.data.entities.edit_task.ActionIcons
import start.up.tracker.ui.extensions.list.ListExtension
import start.up.tracker.ui.list.adapters.edit_task.ActionsIconsAdapter
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder

class ActionIconsViewHolder(
    private val layoutInflater: LayoutInflater,
    parent: ViewGroup
) : BaseViewHolder(layoutInflater, parent, R.layout.base_list_item),
    ActionIconViewHolder.ActionIconClickListener {

    private var binding = BaseListItemBinding.bind(itemView)

    private lateinit var data: ActionIcons

    private lateinit var listener: ActionIconViewHolder.ActionIconClickListener
    private lateinit var adapter: ActionsIconsAdapter
    private var listExtension: ListExtension? = null

    init {
        initAdapter()
    }

    override fun onActionClickListener(actionIconId: Int) {
        listener.onActionClickListener(actionIconId)
    }

    fun bind(listItem: ListItem, listener: ActionIconViewHolder.ActionIconClickListener) {
        this.data = listItem.data as ActionIcons
        this.listener = listener

        updateItems()
    }

    private fun updateItems() {
        val actionIconsListItems = data.icons.map {
            val item = ListItem(
                type = ListItemTypes.BUTTON,
                data = it
            )
            item
        }
        adapter.updateItems(actionIconsListItems)
    }

    private fun initAdapter() {
        adapter = ActionsIconsAdapter(
            layoutInflater = layoutInflater,
            actionIconClickListener = this,
        )

        listExtension = ListExtension(binding.baseList)
        listExtension?.setHorizontalLayoutManager()
        listExtension?.setAdapter(adapter)
    }
}
