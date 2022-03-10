package start.up.tracker.ui.list.adapters.edit_task

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.entities.Task
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.forms.ListItem
import start.up.tracker.ui.data.entities.forms.ListItemTypes
import start.up.tracker.ui.list.adapters.base.BaseSequenceAdapter
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.ui.list.view_holders.forms.TextInputViewHolder
import start.up.tracker.ui.list.view_holders.headers.HeaderViewHolder

class EditTaskAdapter(
    layoutInflater: LayoutInflater,
    /*private val textInputListener: TextInputListener,
    private val textInputSelectionListener: TextInputSelectionListener*/
) : BaseSequenceAdapter<ListItem, BaseViewHolder>(layoutInflater) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            ITEM_TASK_DATA_HEADER, ITEM_PRIORITY_HEADER, ITEM_CATEGORY_HEADER,
            ITEM_TIME_HEADER, ITEM_DATE_HEADER ->
                return HeaderViewHolder(layoutInflater, parent)
            ITEM_INPUT_TITLE, ITEM_INPUT_DESCRIPTION ->
                return TextInputViewHolder(layoutInflater, parent)
            /*ITEM_SELECTION_TIME_START, ITEM_SELECTION_TIME_END,
            ITEM_SELECTION_DATE, ITEM_SELECTION_REPEAT ->
                return SelectInputViewHolder(layoutInflater, parent)
            ITEM_PRIORITIES_LIST ->
                return PriorityViewHolder(layoutInflater, parent)
            ITEM_CATEGORIES_LIST ->
                return CategoriesViewHolder(layoutInflater, parent)*/
            else -> throwUnknownViewHolderTypeException()
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, item: ListItem) {
        when (holder.itemViewType) {
            ITEM_TASK_DATA_HEADER, ITEM_PRIORITY_HEADER, ITEM_CATEGORY_HEADER,
            ITEM_TIME_HEADER, ITEM_DATE_HEADER ->
                (holder as HeaderViewHolder).bind(item)
            ITEM_INPUT_TITLE, ITEM_INPUT_DESCRIPTION ->
                (holder as TextInputViewHolder).bind(item, /*textInputListener*/)
            /*ITEM_SELECTION_TIME_START, ITEM_SELECTION_TIME_END,
            ITEM_SELECTION_DATE, ITEM_SELECTION_REPEAT ->
                (holder as SelectInputViewHolder).bind(item, textInputListener)
            ITEM_PRIORITIES_LIST ->
                (holder as PriorityViewHolder).bind(item)
            ITEM_CATEGORIES_LIST ->
                (holder as CategoriesViewHolder).bind(item)*/
        }
    }

    override fun getItemViewType(item: ListItem): Int {
        return when (item.type) {
            ListItemTypes.HEADER -> getHeaderItemViewType(item)
            ListItemTypes.INPUT_TEXT -> getInputTextItemViewTime(item)
            ListItemTypes.SELECT -> getSelectionItemViewType(item)
            ListItemTypes.LIST -> getListItemViewType(item)
            else -> NOT_FOUND
        }
    }

    override fun getTypeSequence() = intArrayOf(
        ITEM_TASK_DATA_HEADER,
        ITEM_INPUT_TITLE,
        ITEM_INPUT_DESCRIPTION,
        ITEM_PRIORITY_HEADER,
        /*ITEM_PRIORITIES_LIST,
        ITEM_CATEGORY_HEADER,
        ITEM_CATEGORIES_LIST,
        ITEM_TIME_HEADER,
        ITEM_SELECTION_TIME_START,
        ITEM_SELECTION_TIME_END,
        ITEM_DATE_HEADER,
        ITEM_SELECTION_DATE,
        ITEM_SELECTION_REPEAT*/
    )

    fun addListItems(listItems: List<ListItem>) {
        updateItems(listItems)
    }

    private fun getInputTextItemViewTime(item: ListItem): Int {
        return when (item.id) {
            ListItemIds.TASK_TITLE -> ITEM_INPUT_TITLE
            ListItemIds.TASK_DESCRIPTION -> ITEM_INPUT_DESCRIPTION
            else -> NOT_FOUND
        }
    }

    private fun getListItemViewType(item: ListItem): Int {
        return when (item.id) {
            ListItemIds.TASK_CATEGORIES -> ITEM_CATEGORIES_LIST
            ListItemIds.TASK_PRIORITIES -> ITEM_PRIORITIES_LIST
            else -> NOT_FOUND
        }
    }

    private fun getSelectionItemViewType(item: ListItem): Int {
        return when (item.id) {
            ListItemIds.TASK_TIME_START -> ITEM_SELECTION_TIME_START
            ListItemIds.TASK_TIME_END -> ITEM_SELECTION_TIME_END
            ListItemIds.TASK_DATE -> ITEM_SELECTION_DATE
            ListItemIds.TASK_REPEAT -> ITEM_SELECTION_REPEAT
            else -> NOT_FOUND
        }
    }

    private fun getHeaderItemViewType(item: ListItem): Int {
        return when (item.id) {
            ListItemIds.TASK_DATA_HEADER -> ITEM_TASK_DATA_HEADER
            ListItemIds.TASK_PRIORITY_HEADER -> ITEM_PRIORITY_HEADER
            ListItemIds.TASK_CATEGORY_HEADER -> ITEM_CATEGORY_HEADER
            ListItemIds.TASK_TIME_HEADER -> ITEM_TIME_HEADER
            ListItemIds.TASK_DATE_HEADER -> ITEM_DATE_HEADER
            else -> NOT_FOUND
        }
    }

    private companion object {
        const val ITEM_TASK_DATA_HEADER = 0
        const val ITEM_INPUT_TITLE = 1
        const val ITEM_INPUT_DESCRIPTION = 2
        const val ITEM_PRIORITY_HEADER = 3
        const val ITEM_PRIORITIES_LIST = 4
        const val ITEM_CATEGORY_HEADER = 5
        const val ITEM_CATEGORIES_LIST = 6
        const val ITEM_TIME_HEADER = 7
        const val ITEM_SELECTION_TIME_START = 8
        const val ITEM_SELECTION_TIME_END = 9
        const val ITEM_DATE_HEADER = 10
        const val ITEM_SELECTION_DATE = 11
        const val ITEM_SELECTION_REPEAT = 12
    }
}
