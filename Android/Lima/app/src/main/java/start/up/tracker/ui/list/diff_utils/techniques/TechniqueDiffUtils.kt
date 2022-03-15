package start.up.tracker.ui.list.diff_utils.techniques

import androidx.recyclerview.widget.DiffUtil
import start.up.tracker.entities.Technique
import start.up.tracker.ui.data.entities.ListItem

class TechniqueDiffUtils(
    private val oldList: List<ListItem>,
    private val newList: List<ListItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask = oldList[oldItemPosition].data as Technique
        val newTask = newList[newItemPosition].data as Technique
        return oldTask.id == newTask.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask = oldList[oldItemPosition].data as Technique
        val newTask = newList[newItemPosition].data as Technique
        return oldTask == newTask
    }
}
