package start.up.tracker.ui.list.view_holders.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Базовый ViewHolder
 */
open class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    constructor(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        @LayoutRes contentLayoutId: Int
    ) : this(layoutInflater.inflate(contentLayoutId, parent, false))

    /**
     * Отключить свойство @StaggeredGridLayoutManager
     */
    protected fun disableStaggeredGridAbility() {
        val layoutParams = itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = true
        }
    }
}
