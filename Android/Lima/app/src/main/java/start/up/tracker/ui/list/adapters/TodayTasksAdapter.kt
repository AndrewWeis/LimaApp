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

class TodayTasksAdapter(
    private val listener: OnTaskClickListener
) : ListAdapter<Task, TodayTasksAdapter.TasksViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding =
            ExtendedTaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class TasksViewHolder(private val binding: ExtendedTaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val todayTask = getItem(position)
                        listener.onTaskClick(todayTask)
                    }
                }

                taskCheckBox.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val todayTask = getItem(position)
                        listener.onCheckBoxClick(todayTask.copy(completed = taskCheckBox.isChecked))
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
                taskCategoryText.setTextColor(task.color!!)
                taskCategoryImage.background.setTint(task.color)

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
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task) =
            oldItem == newItem
    }
}
