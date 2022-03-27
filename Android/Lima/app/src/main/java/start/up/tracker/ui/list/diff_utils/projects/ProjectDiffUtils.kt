package start.up.tracker.ui.list.diff_utils.projects

import androidx.recyclerview.widget.DiffUtil
import start.up.tracker.entities.Project
import start.up.tracker.ui.data.entities.ListItem

class ProjectDiffUtils(
    private val oldList: List<ListItem>,
    private val newList: List<ListItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask = oldList[oldItemPosition].data as Project
        val newTask = newList[newItemPosition].data as Project
        return oldTask.projectId == newTask.projectId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask = oldList[oldItemPosition].data as Project
        val newTask = newList[newItemPosition].data as Project
        return oldTask == newTask
    }
}
