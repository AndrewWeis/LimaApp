package start.up.tracker.ui.list.view_holders.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.ExtendedTaskItemBinding
import start.up.tracker.entities.Task
import start.up.tracker.ui.data.entities.forms.ListItem
import start.up.tracker.ui.data.entities.forms.Settings
import start.up.tracker.ui.list.view_holders.OnTaskClickListener

class ExtendedTaskViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup
) : TaskViewHolder(layoutInflater, parent, R.layout.extended_task_item) {

    private var binding: ExtendedTaskItemBinding = ExtendedTaskItemBinding.bind(itemView)

    private lateinit var settings: Settings
    private lateinit var task: Task

    override fun bind(listItem: ListItem, listener: OnTaskClickListener) {
        super.bind(listItem, listener)
        this.task = listItem.data as Task
        this.settings = listItem.settings

        setCategory()
    }

    private fun setCategory() {
        binding.apply {
            taskCategoryText.text = task.categoryName

            task.categoryColor?.let {
                taskCategoryText.setTextColor(it)
                taskCategoryImage.background.setTint(it)
            }
        }
    }
}
