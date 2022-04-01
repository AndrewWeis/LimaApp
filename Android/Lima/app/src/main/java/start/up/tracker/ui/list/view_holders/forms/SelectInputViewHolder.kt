package start.up.tracker.ui.list.view_holders.forms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.list.view_holders.forms.base.BaseInputViewHolder
import start.up.tracker.ui.views.forms.base.BaseInputView.TextInputListener

class SelectInputViewHolder(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup
) : BaseInputViewHolder(layoutInflater, viewGroup) {

    fun bind(listItem: ListItem, listener: TextInputSelectionListener) {
        setDefaultEndIcon(listItem)
        super.bind(listItem, listener)
        disableInputMode()
        setListener(listItem, listener)
    }

    private fun setDefaultEndIcon(listItem: ListItem) {
        if (listItem.settings.icon != null) {
            return
        }

        listItem.settings.icon = R.drawable.ic_arrow_down
    }

    private fun setListener(listItem: ListItem, listener: TextInputListener?) {
        listener?.let {
            setTextInputEditTextOnClickListener(getOnClickListener(listItem, it))
        }
    }

    private fun getOnClickListener(
        listItem: ListItem,
        listener: TextInputListener
    ): View.OnClickListener {
        return View.OnClickListener {
            (listener as TextInputSelectionListener).onTextInputSelectionClick(listItem)
        }
    }

    interface TextInputSelectionListener : TextInputListener {
        fun onTextInputSelectionClick(listItem: ListItem)
    }
}
