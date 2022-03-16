package start.up.tracker.ui.list.adapters.edit_task

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.ListItemTypes
import start.up.tracker.ui.list.adapters.base.BaseSequenceAdapter
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.ui.list.view_holders.edit_task.ChipsViewHolder
import start.up.tracker.ui.list.view_holders.forms.SelectInputViewHolder
import start.up.tracker.ui.list.view_holders.forms.TextInputViewHolder
import start.up.tracker.ui.list.view_holders.headers.HeaderViewHolder
import start.up.tracker.ui.list.view_holders.tasks.AddSubtaskViewHolder
import start.up.tracker.ui.list.view_holders.tasks.OnTaskClickListener
import start.up.tracker.ui.list.view_holders.tasks.TasksViewHolder
import start.up.tracker.ui.views.forms.base.BaseInputView

class EditTaskAdapter(
    layoutInflater: LayoutInflater,
    private val textInputListener: BaseInputView.TextInputListener,
    private val textInputSelectionListener: SelectInputViewHolder.TextInputSelectionListener,
    private val categoriesViewHolderListener: ChipsViewHolder.CategoriesViewHolderListener,
    private val onTaskClickListener: OnTaskClickListener,
    private val onAddSubtaskListener: AddSubtaskViewHolder.OnAddSubtaskClickListener,
) : BaseSequenceAdapter<ListItem, BaseViewHolder>(layoutInflater) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            ITEM_PRIORITY_HEADER, ITEM_CATEGORY_HEADER,
            ITEM_TIME_HEADER, ITEM_DATE_HEADER, ITEM_SUBTASKS_HEADER ->
                return HeaderViewHolder(layoutInflater, parent)
            ITEM_INPUT_TITLE, ITEM_INPUT_DESCRIPTION ->
                return TextInputViewHolder(layoutInflater, parent)
            ITEM_SELECTION_TIME_START, ITEM_SELECTION_TIME_END,
            ITEM_SELECTION_DATE, ITEM_SELECTION_REPEAT ->
                return SelectInputViewHolder(layoutInflater, parent)
            ITEM_CATEGORIES_LIST, ITEM_PRIORITIES_LIST ->
                return ChipsViewHolder(layoutInflater, parent)
            ITEM_SUBTASKS_LIST ->
                return TasksViewHolder(layoutInflater, parent)
            ITEM_ADD_SUBTASK_BUTTON ->
                return AddSubtaskViewHolder(layoutInflater, parent)
            else -> throwUnknownViewHolderTypeException()
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, item: ListItem) {
        when (holder.itemViewType) {
            ITEM_PRIORITY_HEADER, ITEM_CATEGORY_HEADER,
            ITEM_TIME_HEADER, ITEM_DATE_HEADER, ITEM_SUBTASKS_HEADER ->
                (holder as HeaderViewHolder).bind(item)
            ITEM_INPUT_TITLE, ITEM_INPUT_DESCRIPTION ->
                (holder as TextInputViewHolder).bind(item, textInputListener)
            ITEM_SELECTION_TIME_START, ITEM_SELECTION_TIME_END,
            ITEM_SELECTION_DATE, ITEM_SELECTION_REPEAT ->
                (holder as SelectInputViewHolder).bind(item, textInputSelectionListener)
            ITEM_CATEGORIES_LIST, ITEM_PRIORITIES_LIST ->
                (holder as ChipsViewHolder).bind(item, categoriesViewHolderListener)
            ITEM_SUBTASKS_LIST ->
                (holder as TasksViewHolder).bind(item, onTaskClickListener)
            ITEM_ADD_SUBTASK_BUTTON ->
                (holder as AddSubtaskViewHolder).bind(onAddSubtaskListener)
        }
    }

    override fun getItemViewType(item: ListItem): Int {
        return when (item.type) {
            ListItemTypes.HEADER -> getHeaderItemViewType(item)
            ListItemTypes.INPUT_TEXT -> getInputTextItemViewTime(item)
            ListItemTypes.SELECT -> getSelectionItemViewType(item)
            ListItemTypes.LIST -> getListItemViewType(item)
            ListItemTypes.BUTTON -> ITEM_ADD_SUBTASK_BUTTON
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
        ITEM_SELECTION_REPEAT,
        ITEM_SUBTASKS_HEADER,
        ITEM_SUBTASKS_LIST,
        ITEM_ADD_SUBTASK_BUTTON
    )

    fun setSubtasksListItem(listItem: ListItem) {
        updateItem(listItem, ITEM_SUBTASKS_LIST)
    }

    fun setPrioritiesChipListItem(listItem: ListItem) {
        updateItem(listItem, ITEM_PRIORITIES_LIST)
    }

    fun setCategoryChipListItem(listItem: ListItem) {
        updateItem(listItem, ITEM_CATEGORIES_LIST)
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
            ListItemIds.TASK_SUBTASKS -> ITEM_SUBTASKS_LIST
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
            ListItemIds.TASK_PRIORITIES_HEADER -> ITEM_PRIORITY_HEADER
            ListItemIds.TASK_CATEGORIES_HEADER -> ITEM_CATEGORY_HEADER
            ListItemIds.TASK_TIME_HEADER -> ITEM_TIME_HEADER
            ListItemIds.TASK_DATE_HEADER -> ITEM_DATE_HEADER
            ListItemIds.TASK_SUBTASKS_HEADER -> ITEM_SUBTASKS_HEADER
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
        const val ITEM_SUBTASKS_HEADER = 12
        const val ITEM_SUBTASKS_LIST = 13
        const val ITEM_ADD_SUBTASK_BUTTON = 14
    }
}
