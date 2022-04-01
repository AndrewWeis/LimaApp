package start.up.tracker.ui.list.view_holders.forms.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.TextInputItemBinding
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.ui.views.forms.base.BaseInputView
import start.up.tracker.ui.views.forms.base.BaseInputView.TextInputListener

/**
 * Базовый ViewHolder для поля формы ввода текста
 */
open class BaseInputViewHolder(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup
) : BaseViewHolder(layoutInflater, viewGroup, R.layout.text_input_item) {

    private val binding: TextInputItemBinding = TextInputItemBinding.bind(itemView)
    private val baseInputView: BaseInputView = BaseInputView(binding)

    fun bind(listItem: ListItem, listener: TextInputListener) {
        baseInputView.bind(listItem, listener)
    }

    protected fun disableInputMode() {
        baseInputView.disableInputMode()
    }

    protected fun setTextInputEditTextOnClickListener(onClickListener: View.OnClickListener?) {
        baseInputView.setTextInputEditTextOnClickListener(onClickListener)
    }
}
