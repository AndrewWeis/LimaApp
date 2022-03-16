package start.up.tracker.ui.list.view_holders.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder

class AddSubtaskViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup
) : BaseViewHolder(layoutInflater, parent, R.layout.add_subtask_button) {

    private lateinit var listener: OnAddSubtaskClickListener

    fun bind(listener: OnAddSubtaskClickListener) {
        this.listener = listener

        setupListeners()
    }

    private fun setupListeners() {
        itemView.setOnClickListener {
            listener.onAddSubtaskClick()
        }
    }

    interface OnAddSubtaskClickListener {
        fun onAddSubtaskClick()
    }
}