package start.up.tracker.ui.list.view_holders.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.TaskItemBinding
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.list.view_holders.tasks.base.BaseTaskViewHolder

class TaskViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup,
) : BaseTaskViewHolder(layoutInflater, parent, R.layout.task_item) {

    private val binding: TaskItemBinding = TaskItemBinding.bind(itemView)

    override fun bind(listItem: ListItem, listener: OnTaskClickListener) {
        super.bind(listItem, listener)

        setupSubtasks()
        setupCategory()
    }

    private fun setupCategory() {
        if (task.categoryName == null && task.categoryColor == null) {
            binding.taskCategoryText.visibility = View.GONE
            binding.taskCategoryImage.visibility = View.GONE
            return
        }

        binding.apply {
            taskCategoryText.text = task.categoryName
            taskCategoryText.setTextColor(task.categoryColor!!)
            taskCategoryImage.background.setTint(task.categoryColor!!)
        }
    }

    private fun setupSubtasks() {
        if (task.subtasksNumber == 0) {
            binding.subtaskImage.visibility = View.GONE
            binding.subtasksNumberText.visibility = View.GONE
            return
        }

        val subtasksText = "${task.completedSubtasksNumber}/${task.subtasksNumber}"
        binding.subtasksNumberText.text = subtasksText
    }
}
