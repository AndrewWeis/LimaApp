package start.up.tracker.ui.list.adapters.edit_task

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.mvvm.view_models.tasks.base.BaseTasksOperationsViewModel
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.ListItemTypes
import start.up.tracker.ui.list.adapters.base.BaseSequenceAdapter
import start.up.tracker.ui.list.view_holders.add_project.HeaderActionsViewHolder
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.ui.list.view_holders.edit_task.ActionIconViewHolder
import start.up.tracker.ui.list.view_holders.edit_task.ActionIconsViewHolder
import start.up.tracker.ui.list.view_holders.forms.TextInputViewHolder
import start.up.tracker.ui.list.view_holders.tasks.AddSubtaskViewHolder
import start.up.tracker.ui.list.view_holders.tasks.OnTaskClickListener
import start.up.tracker.ui.list.view_holders.tasks.TasksViewHolder
import start.up.tracker.ui.views.forms.base.BaseInputView

class EditTaskAdapter(
    layoutInflater: LayoutInflater,
    private val viewModel: BaseTasksOperationsViewModel,
    private val textInputListener: BaseInputView.TextInputListener,
    private val onTaskClickListener: OnTaskClickListener,
    private val onAddSubtaskListener: AddSubtaskViewHolder.OnAddSubtaskClickListener,
    private val actionIconClickListener: ActionIconViewHolder.ActionIconClickListener,
    private val addProjectActionsClickListener: HeaderActionsViewHolder.AddProjectActionClickListener,
) : BaseSequenceAdapter<ListItem, BaseViewHolder>(layoutInflater) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            ITEM_ACTIONS_HEADER ->
                return HeaderActionsViewHolder(layoutInflater, parent)
            ITEM_INPUT_TITLE, ITEM_INPUT_DESCRIPTION ->
                return TextInputViewHolder(layoutInflater, parent)
            ITEM_SUBTASKS_LIST ->
                return TasksViewHolder(layoutInflater, parent)
            ITEM_ADD_SUBTASK_BUTTON ->
                return AddSubtaskViewHolder(layoutInflater, parent)
            ITEM_ACTIONS_ICONS_LIST ->
                return ActionIconsViewHolder(layoutInflater, parent)
            else -> throwUnknownViewHolderTypeException()
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, item: ListItem) {
        when (holder.itemViewType) {
            ITEM_ACTIONS_HEADER ->
                (holder as HeaderActionsViewHolder).bind(item, addProjectActionsClickListener)
            ITEM_INPUT_TITLE, ITEM_INPUT_DESCRIPTION ->
                (holder as TextInputViewHolder).bind(item, textInputListener)
            ITEM_SUBTASKS_LIST ->
                (holder as TasksViewHolder).bind(item, viewModel, onTaskClickListener)
            ITEM_ADD_SUBTASK_BUTTON ->
                (holder as AddSubtaskViewHolder).bind(onAddSubtaskListener)
            ITEM_ACTIONS_ICONS_LIST ->
                (holder as ActionIconsViewHolder).bind(item, actionIconClickListener)
        }
    }

    override fun getItemViewType(item: ListItem): Int {
        return when (item.type) {
            ListItemTypes.HEADER -> ITEM_ACTIONS_HEADER
            ListItemTypes.INPUT_TEXT -> getInputTextItemViewTime(item)
            ListItemTypes.SELECT -> ITEM_SELECTION_REPEAT
            ListItemTypes.LIST -> getListItemViewType(item)
            ListItemTypes.BUTTON -> ITEM_ADD_SUBTASK_BUTTON
            else -> NOT_FOUND
        }
    }

    override fun getTypeSequence() = intArrayOf(
        ITEM_ACTIONS_HEADER,
        ITEM_INPUT_TITLE,
        ITEM_INPUT_DESCRIPTION,
        ITEM_SELECTION_REPEAT,
        ITEM_ACTIONS_ICONS_LIST,
        ITEM_SUBTASKS_LIST,
        ITEM_ADD_SUBTASK_BUTTON,
    )

    fun setDescriptionItem(listItem: ListItem) {
        updateItem(listItem, ITEM_INPUT_DESCRIPTION)
    }

    fun setAddSubtaskButtonListItem(listItem: ListItem) {
        updateItem(listItem, ITEM_ADD_SUBTASK_BUTTON)
    }

    fun setActionsIconsListItem(listItem: ListItem) {
        updateItem(listItem, ITEM_ACTIONS_ICONS_LIST)
    }

    fun setSubtasksListItem(listItem: ListItem) {
        updateItem(listItem, ITEM_SUBTASKS_LIST)
    }

    fun setTitleItem(listItem: ListItem) {
        updateItem(listItem, ITEM_INPUT_TITLE)
    }

    fun setActionsHeaderItem(listItem: ListItem) {
        updateItem(listItem, ITEM_ACTIONS_HEADER)
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
            ListItemIds.TASK_SUBTASKS -> ITEM_SUBTASKS_LIST
            ListItemIds.ACTIONS_ICONS -> ITEM_ACTIONS_ICONS_LIST
            else -> NOT_FOUND
        }
    }

    private companion object {
        const val ITEM_ACTIONS_HEADER = 0
        const val ITEM_INPUT_TITLE = 1
        const val ITEM_INPUT_DESCRIPTION = 2
        const val ITEM_SELECTION_REPEAT = 3
        const val ITEM_ACTIONS_ICONS_LIST = 4
        const val ITEM_SUBTASKS_LIST = 5
        const val ITEM_ADD_SUBTASK_BUTTON = 6
    }
}
