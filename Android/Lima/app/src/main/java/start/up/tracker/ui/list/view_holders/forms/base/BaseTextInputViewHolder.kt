package start.up.tracker.ui.list.view_holders.forms.base

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.TextInputItemBinding
import start.up.tracker.ui.data.entities.forms.ListItem
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.ui.views.forms.base.BaseInputView
import start.up.tracker.ui.views.forms.base.BaseTextInputView

/**
 * Базовый ViewHolder для полей вввода
 */
abstract class BaseTextInputViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    BaseViewHolder(inflater, parent, R.layout.text_input_item) {

    private val binding: TextInputItemBinding = TextInputItemBinding.bind(itemView)
    private val baseTextInputView: BaseTextInputView = BaseTextInputView(binding)

    fun bind(listItem: ListItem, listener: BaseInputView.TextInputListener) {
        baseTextInputView.bind(listItem, listener)
    }
}
