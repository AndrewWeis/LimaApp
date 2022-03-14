package start.up.tracker.ui.list.adapters.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.forms.ListItem
import start.up.tracker.ui.list.adapters.base.BaseAdapter
import start.up.tracker.ui.list.diff_utils.tasks.TaskDiffUtils
import start.up.tracker.ui.list.view_holders.OnTaskClickListener
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.ui.list.view_holders.tasks.TaskViewHolder

class ProjectsTasksAdapter(
    layoutInflater: LayoutInflater,
    private val listener: OnTaskClickListener
) : BaseAdapter<ListItem, BaseViewHolder>(layoutInflater) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            TYPE_TASK, TYPE_SUBTASK -> TaskViewHolder(layoutInflater, parent)
            else -> throwUnknownViewHolderTypeException()
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, item: ListItem) {
        when (holder.itemViewType) {
            TYPE_TASK, TYPE_SUBTASK -> (holder as TaskViewHolder).bind(item, listener)
        }
    }

    override fun getItemViewType(item: ListItem): Int {
        return when (item.id) {
            ListItemIds.TASK -> TYPE_TASK
            ListItemIds.SUBTASK -> TYPE_SUBTASK
            else -> NOT_FOUND
        }
    }

    override fun updateItems(items: List<ListItem>) {
        val diffUtils = TaskDiffUtils(getItems(), items)
        val diffResult = DiffUtil.calculateDiff(diffUtils)
        getItems().clear()
        getItems().addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    fun addListItems(listItems: List<ListItem>) {
        updateItems(listItems)
    }

    private companion object {
        const val TYPE_TASK = 0
        const val TYPE_SUBTASK = 1
    }
}
