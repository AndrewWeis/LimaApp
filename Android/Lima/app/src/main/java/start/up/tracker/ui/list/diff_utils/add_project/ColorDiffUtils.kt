package start.up.tracker.ui.list.diff_utils.add_project

import androidx.recyclerview.widget.DiffUtil
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.add_project.ColorData

class ColorDiffUtils(
    private val oldList: List<ListItem>,
    private val newList: List<ListItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask = oldList[oldItemPosition].data as ColorData
        val newTask = newList[newItemPosition].data as ColorData
        return oldTask.colorRes == newTask.colorRes
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask = oldList[oldItemPosition].data as ColorData
        val newTask = newList[newItemPosition].data as ColorData
        return oldTask == newTask
    }
}
