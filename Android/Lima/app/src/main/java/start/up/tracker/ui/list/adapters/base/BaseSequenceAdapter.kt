package start.up.tracker.ui.list.adapters.base

import android.view.LayoutInflater
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder

/**
 * Базовый Adapter c фиксированной последовательностью типов ViewHolder
 *
 * @param <ItemClass> класс элементов списка
 * @param <VH>        класс ViewHolder
 */
abstract class BaseSequenceAdapter<ItemClass, VH : BaseViewHolder>(layoutInflater: LayoutInflater) :
    BaseAdapter<ItemClass, VH>(layoutInflater) {

    /**
     * Ключ - тип ViewHolder, значение - порядковый номер типа ViewHolder.
     * Порядковый номер определяется последовательностью типов ({@link #getTypeSequence()}).
     * Эта структура нужна для быстрого доступа к порядковым номерам без необходимости
     * перебора последовательности ({@link #getTypeSequence()}).
     */
    private var typeIndexes: MutableMap<Int, Int> = mutableMapOf()

    init {
        val typesSequence = getTypeSequence()
        for (i in typesSequence.indices) {
            typeIndexes[typesSequence[i]] = i
        }
    }

    /**
     * Получить последовательность типов
     *
     * @return последовательность типов
     */
    protected abstract fun getTypeSequence(): IntArray

    /**
     * Задавание элемента в списке по типу.
     *
     * @param item элемент, который необходимо задать
     * @param type тип ViewHolder
     */
    protected fun updateItem(item: ItemClass, type: Int) {
        updateItem(item, getDesiredItemPosition(type), type)
    }

    /**
     * Задавание элементов в списке по типу.
     *
     * @param objects объекты, которые необходимо задать
     * @param type    тип ViewHolder
     */
    protected fun updateItems(objects: List<ItemClass>, type: Int) {
        removeItemsByType(type)
        addItems(objects, type)
    }

    /**
     * Получить желаемую позицию элемента по типу.
     * Желаемая = фактической, если элемент есть в списке.
     *
     * @param type тип ViewHolder
     * @return позиция элемента
     */
    private fun getDesiredItemPosition(type: Int): Int {
        getItems().forEachIndexed { index, item ->
            val currentType = getItemViewType(item)
            val currentTypeIndex = typeIndexes[currentType]
            val typeIndex = typeIndexes[type]
            if (currentTypeIndex!! >= typeIndex!!) {
                return index
            }
        }

        return itemCount
    }

    /**
     * Добавление элементов в список по типу
     *
     * @param addingItems добавляемые элементы
     * @param type        тип ViewHolder
     */
    private fun addItems(addingItems: List<ItemClass>, type: Int) {
        val addingPosition = getDesiredItemPosition(type)
        getItems().addAll(addingPosition, addingItems)
        notifyItemRangeInserted(addingPosition, addingItems.size)
    }
}
