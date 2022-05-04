package start.up.tracker.ui.list.view_holders.headers

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.EditTaskHeaderItemBinding
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.header.Header
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder

class HeaderViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup
) : BaseViewHolder(layoutInflater, parent, R.layout.edit_task_header_item) {

    private var binding: EditTaskHeaderItemBinding = EditTaskHeaderItemBinding.bind(itemView)
    private lateinit var header: Header

    fun bind(listItem: ListItem) {
        this.header = listItem.data as Header

        setHeaderTitle()
    }

    private fun setHeaderTitle() {
        binding.editTaskHeaderText.text = header.title
    }
}
