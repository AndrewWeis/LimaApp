package start.up.tracker.ui.list.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import start.up.tracker.databinding.ExtendedTaskItemBinding
import start.up.tracker.entities.Task
import start.up.tracker.ui.list.view_holders.OnTaskClickListener
import start.up.tracker.utils.chooseIconDrawable


class UpcomingSectionAdapter(
    private val listener: OnTaskClickListener
) : ListAdapter<Task, UpcomingSectionAdapter.SectionsViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionsViewHolder {
        val binding = ExtendedTaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SectionsViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class SectionsViewHolder(private val binding: ExtendedTaskItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onTaskClick(task)
                    }
                }

                taskCheckBox.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onCheckBoxClick(task.copy(completed = taskCheckBox.isChecked))
                    }
                }
            }
        }

        fun bind(task: Task) {
            binding.apply {
                taskCheckBox.isChecked = task.completed
                taskTitleText.text = task.title
                taskTitleText.paint.isStrikeThruText = task.completed

                taskCategoryText.text = task.categoryName
                taskCategoryText.setTextColor(task.categoryColor!!)
                taskCategoryImage.background.setTint(task.categoryColor)

                if (task.priority == 4) {
                    priorityImage.visibility = View.GONE
                } else {
                    priorityImage.setImageResource(chooseIconDrawable(task.priority))
                }
            }
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) =
            oldItem.taskId == newItem.taskId

        override fun areContentsTheSame(oldItem: Task, newItem: Task) =
            oldItem == newItem
    }
}
