package start.up.tracker.ui.list.adapters.base

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder

/**
 * Базовый Adapter
 */
abstract class BaseAdapter<ItemClass, VH : BaseViewHolder>(
    protected val layoutInflater: LayoutInflater
) : RecyclerView.Adapter<VH>() {

    private var items: MutableList<ItemClass> = mutableListOf()

    protected abstract fun onBindViewHolder(holder: VH, item: ItemClass)

    override fun getItemCount() = items.size

    final override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return getItemViewType(item)
    }

    final override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        onBindViewHolder(holder, item)
    }

    fun updateItems(items: List<ItemClass>) {
        this.items = items.toMutableList()
        notifyDataSetChanged()
    }

    protected open fun getItemViewType(item: ItemClass): Int = 0

    /**
     * Выкинуть исплючение для несуществующего типа @ViewHolder
     */
    protected fun throwUnknownViewHolderTypeException(): BaseViewHolder {
        throw getUnknownViewHolderTypeException()
    }

    /**
     * Возвращает исключение о неизвестном типе ViewHolder`а
     *
     * @return исключение
     */
    protected fun getUnknownViewHolderTypeException(): IllegalArgumentException {
        return IllegalArgumentException("Unknown ViewHolder Type!")
    }

    protected fun updateItem(item: ItemClass, position: Int, type: Int) {
        val startPosition: Int = items.size

        if (items.size <= position) {
            items.add(item)
            notifyItemInserted(startPosition)
            return
        }

        val typeForPosition = getItemViewType(position)

        if (type == typeForPosition) {
            items[position] = item
            notifyItemChanged(position)
            return
        }

        items.add(position, item)
        notifyItemInserted(startPosition)
    }

    protected fun removeItemsByTypes(vararg types: Int) {
        items = (items.filterIndexed { index, _ ->
            val type = getItemViewType(index)
            !types.contains(type)
        }).toMutableList()

        notifyDataSetChanged()
    }

    protected fun removeItemsByType(type: Int) {
        removeItemsByTypes(type)
    }

    protected fun addItems(items: List<ItemClass>) {
        val startPosition = this.items.size
        this.items.addAll(items)
        notifyItemRangeInserted(startPosition, items.size)
    }

    protected fun getItem(position: Int) = items[position]

    protected fun getItems() = items

    protected companion object {
        const val NOT_FOUND = -1;
    }
}
