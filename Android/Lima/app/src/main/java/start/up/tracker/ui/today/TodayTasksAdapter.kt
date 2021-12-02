package start.up.tracker.ui.today

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import start.up.tracker.data.models.TodayTask
import start.up.tracker.databinding.ItemTaskTodayBinding


class TodayTasksAdapter(private val listener: OnItemClickListener) : ListAdapter<TodayTask,
        TodayTasksAdapter.TasksViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding = ItemTaskTodayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class TasksViewHolder(private val binding: ItemTaskTodayBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val todayTask = getItem(position)
                        listener.onItemClick(todayTask)
                    }
                }

                checkBoxCompleted.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val todayTask = getItem(position)
                        listener.onCheckBoxClick(todayTask, checkBoxCompleted.isChecked)
                    }
                }
            }
        }

        fun bind(todayTask: TodayTask) {
            binding.apply {
                checkBoxCompleted.isChecked = todayTask.completed
                textViewName.text = todayTask.taskName
                textViewName.paint.isStrikeThruText = todayTask.completed
                labelPriority.isVisible = todayTask.important
                textCategoryName.text = todayTask.categoryName
                textCategoryName.setTextColor(todayTask.color)
                categoryCircle.background.setTint(todayTask.color)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(todayTask: TodayTask)
        fun onCheckBoxClick(todayTask: TodayTask, isChecked: Boolean)
    }

    class DiffCallback : DiffUtil.ItemCallback<TodayTask>() {
        override fun areItemsTheSame(oldItem: TodayTask, newItem: TodayTask) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TodayTask, newItem: TodayTask) =
            oldItem == newItem
    }
}