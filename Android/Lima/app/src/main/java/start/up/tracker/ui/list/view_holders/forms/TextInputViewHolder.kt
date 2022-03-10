package start.up.tracker.ui.list.view_holders.forms

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.TextInputItemBinding
import start.up.tracker.ui.data.entities.forms.ListItem
import start.up.tracker.ui.data.entities.forms.Settings
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import kotlin.properties.Delegates.notNull

class TextInputViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup
) : BaseViewHolder(layoutInflater, parent, R.layout.text_input_item) {

    private val binding: TextInputItemBinding = TextInputItemBinding.bind(itemView)
    private var title by notNull<String>()
    private lateinit var settings: Settings

    fun bind(listItem: ListItem) {
        this.title = listItem.data as String
        this.settings = listItem.settings
    }
}
