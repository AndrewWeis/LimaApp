package start.up.tracker.ui.today

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import start.up.tracker.R
import start.up.tracker.data.models.TodayTask
import start.up.tracker.databinding.ItemTaskCalendarBinding


class CalendarTasksAdapter(private val listener: OnItemClickListener) : ListAdapter<TodayTask,
        CalendarTasksAdapter.TasksViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding = ItemTaskCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val currentItem = getItem(position)
        val nextItem: TodayTask? = if (position + 1 < itemCount) { getItem(position + 1) } else { null }
        holder.bind(currentItem, nextItem)
    }

    inner class TasksViewHolder(private val binding: ItemTaskCalendarBinding) : RecyclerView.ViewHolder(binding.root) {

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

        fun bind(todayTask: TodayTask, nextTask: TodayTask?) {
            binding.apply {
                checkBoxCompleted.isChecked = todayTask.completed
                textViewName.text = todayTask.taskName
                textViewName.paint.isStrikeThruText = todayTask.completed
                if (todayTask.priority == 4) {
                    icPriority.visibility = View.GONE
                } else {
                    val color = chooseColorPriority(todayTask.priority)
                    icPriority.setBackgroundColor(Color.parseColor(color))
                }

                val layoutParams: ViewGroup.MarginLayoutParams = binding.cardTaskCalendar.layoutParams as ViewGroup.MarginLayoutParams
                var space = 1
                if (nextTask != null) {
                    space = (nextTask.timeStartInt - todayTask.timeEndInt) * 30
                }
                layoutParams.bottomMargin = convertDpToPx(space)
                val height = (todayTask.timeEndInt - todayTask.timeStartInt) * 30 - 1
                layoutParams.height = convertDpToPx(height)
                binding.cardTaskCalendar.requestLayout()
            }
        }

        private fun convertDpToPx(dp: Int) = (dp * Resources.getSystem().displayMetrics.density).toInt()

        private fun chooseColorPriority(priority: Int): String {
            return when(priority) {
                1 -> "#FC3131"
                2 -> "#FC8631"
                3 -> "#40C686"
                else -> "#000000" // This should never be reached
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