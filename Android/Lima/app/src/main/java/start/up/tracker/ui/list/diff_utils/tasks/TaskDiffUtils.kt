package start.up.tracker.ui.list.diff_utils.tasks

import androidx.recyclerview.widget.DiffUtil
import start.up.tracker.entities.Task
import start.up.tracker.ui.data.entities.forms.ListItem

class TaskDiffUtils(
    private val oldList: List<ListItem>,
    private val newList: List<ListItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask = oldList[oldItemPosition].data as Task
        val newTask = newList[newItemPosition].data as Task
        return oldTask.id == newTask.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask = oldList[oldItemPosition].data as Task
        val newTask = newList[newItemPosition].data as Task
        return oldTask == newTask
    }
}
