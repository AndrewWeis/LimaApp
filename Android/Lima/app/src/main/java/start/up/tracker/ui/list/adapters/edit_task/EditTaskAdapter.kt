package start.up.tracker.ui.list.adapters.edit_task

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.ListItemTypes
import start.up.tracker.ui.list.adapters.base.BaseSequenceAdapter
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.ui.list.view_holders.forms.SelectInputViewHolder
import start.up.tracker.ui.list.view_holders.forms.TextInputViewHolder
import start.up.tracker.ui.list.view_holders.headers.HeaderViewHolder
import start.up.tracker.ui.views.forms.base.BaseInputView

class EditTaskAdapter(
    layoutInflater: LayoutInflater,
    private val textInputListener: BaseInputView.TextInputListener,
    private val textInputSelectionListener: SelectInputViewHolder.TextInputSelectionListener,
) : BaseSequenceAdapter<ListItem, BaseViewHolder>(layoutInflater) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            ITEM_PRIORITY_HEADER, ITEM_CATEGORY_HEADER,
            ITEM_TIME_HEADER, ITEM_DATE_HEADER ->
                return HeaderViewHolder(layoutInflater, parent)
            ITEM_INPUT_TITLE, ITEM_INPUT_DESCRIPTION ->
                return TextInputViewHolder(layoutInflater, parent)
            ITEM_SELECTION_TIME_START, ITEM_SELECTION_TIME_END,
            ITEM_SELECTION_DATE, ITEM_SELECTION_REPEAT ->
                return SelectInputViewHolder(layoutInflater, parent)
            /*ITEM_PRIORITIES_LIST ->
                return PriorityViewHolder(layoutInflater, parent)
            ITEM_CATEGORIES_LIST ->
                return CategoriesViewHolder(layoutInflater, parent)*/
            else -> throwUnknownViewHolderTypeException()
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, item: ListItem) {
        when (holder.itemViewType) {
            ITEM_PRIORITY_HEADER, ITEM_CATEGORY_HEADER,
            ITEM_TIME_HEADER, ITEM_DATE_HEADER ->
                (holder as HeaderViewHolder).bind(item)
            ITEM_INPUT_TITLE, ITEM_INPUT_DESCRIPTION ->
                (holder as TextInputViewHolder).bind(item, textInputListener)
            ITEM_SELECTION_TIME_START, ITEM_SELECTION_TIME_END,
            ITEM_SELECTION_DATE, ITEM_SELECTION_REPEAT ->
                (holder as SelectInputViewHolder).bind(item, textInputSelectionListener)
            /*ITEM_PRIORITIES_LIST ->
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
        ITEM_INPUT_TITLE,
        ITEM_INPUT_DESCRIPTION,
        ITEM_PRIORITY_HEADER,
        ITEM_PRIORITIES_LIST,
        ITEM_CATEGORY_HEADER,
        ITEM_CATEGORIES_LIST,
        ITEM_TIME_HEADER,
        ITEM_SELECTION_TIME_START,
        ITEM_SELECTION_TIME_END,
        ITEM_DATE_HEADER,
        ITEM_SELECTION_DATE,
        ITEM_SELECTION_REPEAT
    )

    fun addListItems(listItems: List<ListItem>) {
        updateItems(listItems)
    }

    fun setTitleItem(listItem: ListItem) {
        updateItem(listItem, ITEM_INPUT_TITLE)
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
            ListItemIds.TASK_PRIORITY_HEADER -> ITEM_PRIORITY_HEADER
            ListItemIds.TASK_CATEGORY_HEADER -> ITEM_CATEGORY_HEADER
            ListItemIds.TASK_TIME_HEADER -> ITEM_TIME_HEADER
            ListItemIds.TASK_DATE_HEADER -> ITEM_DATE_HEADER
            else -> NOT_FOUND
        }
    }

    private companion object {
        const val ITEM_INPUT_TITLE = 0
        const val ITEM_INPUT_DESCRIPTION = 1
        const val ITEM_PRIORITY_HEADER = 2
        const val ITEM_PRIORITIES_LIST = 3
        const val ITEM_CATEGORY_HEADER = 4
        const val ITEM_CATEGORIES_LIST = 5
        const val ITEM_TIME_HEADER = 6
        const val ITEM_SELECTION_TIME_START = 7
        const val ITEM_SELECTION_TIME_END = 8
        const val ITEM_DATE_HEADER = 9
        const val ITEM_SELECTION_DATE = 10
        const val ITEM_SELECTION_REPEAT = 11
    }
}
