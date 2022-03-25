package start.up.tracker.ui.list.adapters.add_project

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.ListItemTypes
import start.up.tracker.ui.list.adapters.base.BaseSequenceAdapter
import start.up.tracker.ui.list.view_holders.add_project.AddProjectActionsViewHolder
import start.up.tracker.ui.list.view_holders.add_project.ColorViewHolder
import start.up.tracker.ui.list.view_holders.add_project.ColorsViewHolder
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.ui.list.view_holders.forms.TextInputViewHolder
import start.up.tracker.ui.views.forms.base.BaseInputView

class AddProjectAdapter(
    layoutInflater: LayoutInflater,
    private val textInputListener: BaseInputView.TextInputListener,
    private val addProjectActionsClickListener: AddProjectActionsViewHolder.AddProjectActionClickListener,
    private val colorClickListener: ColorViewHolder.ColorClickListener
) : BaseSequenceAdapter<ListItem, BaseViewHolder>(layoutInflater) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            ITEM_ACTIONS -> return AddProjectActionsViewHolder(layoutInflater, parent)
            ITEM_INPUT_TITLE -> return TextInputViewHolder(layoutInflater, parent)
            ITEM_COLORS_LIST -> return ColorsViewHolder(layoutInflater, parent)
            else -> throwUnknownViewHolderTypeException()
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, item: ListItem) {
        when (holder.itemViewType) {
            ITEM_ACTIONS -> (holder as AddProjectActionsViewHolder).bind(item, addProjectActionsClickListener)
            ITEM_INPUT_TITLE -> (holder as TextInputViewHolder).bind(item, textInputListener)
            ITEM_COLORS_LIST -> (holder as ColorsViewHolder).bind(item, colorClickListener)
        }
    }

    override fun getItemViewType(item: ListItem): Int {
        return when (item.type) {
            ListItemTypes.HEADER -> ITEM_ACTIONS
            ListItemTypes.INPUT_TEXT -> ITEM_INPUT_TITLE
            ListItemTypes.LIST -> ITEM_COLORS_LIST
            else -> NOT_FOUND
        }
    }

    override fun getTypeSequence() = intArrayOf(
        ITEM_ACTIONS,
        ITEM_INPUT_TITLE,
        ITEM_COLORS_LIST
    )

    fun setColorsDataItem(listItem: ListItem) {
        updateItem(listItem, ITEM_COLORS_LIST)
    }

    fun setTitleItem(listItem: ListItem) {
        updateItem(listItem, ITEM_INPUT_TITLE)
    }

    fun setActionsItem(listItem: ListItem) {
        updateItem(listItem, ITEM_ACTIONS)
    }

    private companion object {
        const val ITEM_ACTIONS = 0
        const val ITEM_INPUT_TITLE = 1
        const val ITEM_COLORS_LIST = 2
    }
}
