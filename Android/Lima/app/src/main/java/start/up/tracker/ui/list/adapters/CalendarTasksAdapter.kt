package start.up.tracker.ui.list.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import start.up.tracker.ui.data.constants.TIME_OFFSET
import start.up.tracker.entities.Task
import start.up.tracker.databinding.ItemTaskCalendarBinding
import start.up.tracker.utils.convertDpToPx

class CalendarTasksAdapter(
    private val listener: OnItemClickListener
) : ListAdapter<Task, CalendarTasksAdapter.TasksViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding =
            ItemTaskCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val currentItem = getItem(position)
        val nextItem: Task? = if (position + 1 < itemCount) {
            getItem(position + 1)
        } else {
            null
        }
        holder.bind(currentItem, nextItem, position)
    }

    inner class TasksViewHolder(private val binding: ItemTaskCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {

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

        fun bind(task: Task, nextTask: Task?, position: Int) {
            binding.apply {
                checkBoxCompleted.isChecked = task.completed
                textViewName.text = task.title
                textViewName.paint.isStrikeThruText = task.completed
                if (task.priority == 4) {
                    icPriority.visibility = View.GONE
                } else {
                    val color = chooseColorPriority(task.priority)
                    icPriority.setBackgroundColor(Color.parseColor(color))
                }

                if (task.startTimeInMinutes != null && task.endTimeInMinutes != null) {
                    val layoutParams: ViewGroup.MarginLayoutParams =
                        binding.cardTaskCalendar.layoutParams as ViewGroup.MarginLayoutParams
                    var space = 0
                    var height = task.endTimeInMinutes - task.startTimeInMinutes
                    if (nextTask != null) {
                        space = nextTask.startTimeInMinutes!! - task.endTimeInMinutes
                        if (space == 0) {
                            space = 2
                            height -= 4
                        }
                    }
                    layoutParams.bottomMargin = convertDpToPx(space)
                    layoutParams.height = convertDpToPx(height)
                    if (position == 0 && task.startTimeInMinutes > TIME_OFFSET) {
                        layoutParams.topMargin = convertDpToPx(task.startTimeInMinutes - TIME_OFFSET)
                    }
                    binding.cardTaskCalendar.requestLayout()
                }
            }
        }

        private fun chooseColorPriority(priority: Int): String {
            return when (priority) {
                1 -> "#FC3131"
                2 -> "#FC8631"
                3 -> "#40C686"
                else -> "#000000" // This should never be reached
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(task: Task)
        fun onCheckBoxClick(task: Task, isChecked: Boolean)
    }

    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task) =
            oldItem == newItem
    }
}
