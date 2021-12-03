package start.up.tracker.ui.today

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import start.up.tracker.R
import start.up.tracker.data.models.TodayTask
import start.up.tracker.databinding.ItemTaskTodayBinding
import start.up.tracker.utils.exhaustive


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
                textCategoryName.text = todayTask.categoryName
                textCategoryName.setTextColor(todayTask.color)
                categoryCircle.background.setTint(todayTask.color)

                if (todayTask.priority == 4) {
                    icPriority.visibility = View.GONE
                } else {
                    icPriority.setImageResource(chooseIconDrawable(todayTask.priority))
                }
            }
        }

        private fun chooseIconDrawable(priority: Int): Int {
            return when(priority) {
                1 -> R.drawable.ic_priority_1
                2 -> R.drawable.ic_priority_2
                3 -> R.drawable.ic_priority_3
                else -> R.drawable.ic_android // This should never be reached
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