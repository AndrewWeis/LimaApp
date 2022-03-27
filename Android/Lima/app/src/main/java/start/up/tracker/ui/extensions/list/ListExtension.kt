package start.up.tracker.ui.extensions.list

import android.view.MotionEvent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.list.adapters.base.BaseAdapter
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder

/**
 * Разширение для инициализации @RecyclerView и задания параметров
 */
class ListExtension(private var list: RecyclerView?) {

    /**
     * Необходимость блокировать список
     * true - список заблокирован
     */
    private var shouldLockList = false

    init {
        setVerticalLayoutManager()
        addOnItemTouchListener()
    }

    /**
     * Задать @Adapter
     */
    fun setAdapter(adapter: Adapter<BaseViewHolder>) {
        list?.adapter = adapter
    }

    /**
     * Заблокировать список
     */
    fun lock() {
        shouldLockList = true
    }

    /**
     * Разблокировать список
     */
    fun unlock() {
        shouldLockList = false
    }

    /**
     * Задать @LayoutManager
     */
    fun setLayoutManager(layoutManager: LayoutManager) {
        list?.layoutManager = layoutManager
    }

    /**
     * Задать @GridLayoutManager
     * @param spanCount число строк / столбцов
     * @param orientation ориентация
     */
    fun setGridLayoutManager(spanCount: Int, orientation: Int) {
        list?.layoutManager = getGridLayoutManager(spanCount, orientation)
    }

    /**
     * Задать вертикальный @LayoutManager
     */
    fun setVerticalLayoutManager() {
        list?.layoutManager = getVerticalLayoutManager()
    }

    /**
     * Задать горизонтальный @LayoutManager
     */
    fun setHorizontalLayoutManager() {
        list?.layoutManager = getHorizontalLayoutManager()
    }

    /**
     * Добавляет возможность свайпать вправо или влево элемент списка
     */
    fun attachSwipeToAdapter(
        adapter: BaseAdapter<ListItem, BaseViewHolder>,
        actionOnSwipe: (ListItem) -> Unit
    ) {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: ViewHolder,
                target: ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                val listItem = adapter.getItems().elementAt(viewHolder.adapterPosition)
                actionOnSwipe(listItem)
            }
        }).attachToRecyclerView(list)
    }

    /**
     * Получить вертикальный @LayoutManager
     */
    private fun getVerticalLayoutManager(): LayoutManager {
        return LinearLayoutManager(
            null,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    /**
     * Получить горизонтальный @LayoutManager
     */
    private fun getHorizontalLayoutManager(): LayoutManager {
        return LinearLayoutManager(
            null,
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    /**
     * Получить @GridLayoutManager
     */
    private fun getGridLayoutManager(spanCount: Int, orientation: Int): GridLayoutManager {
        return GridLayoutManager(
            null,
            spanCount,
            orientation,
            false
        )
    }

    /**
     * Добавление слушателя для возможности блокировки списка
     */
    private fun addOnItemTouchListener() {
        list?.addOnItemTouchListener(object : SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                return shouldLockList
            }
        })
    }
}
