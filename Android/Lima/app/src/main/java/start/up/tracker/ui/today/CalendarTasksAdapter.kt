package start.up.tracker.ui.today

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import start.up.tracker.data.models.ExtendedTask
import start.up.tracker.databinding.ItemTaskCalendarBinding
import start.up.tracker.utils.TIME_OFFSET
import start.up.tracker.utils.convertDpToPx


class CalendarTasksAdapter(private val listener: OnItemClickListener) : ListAdapter<ExtendedTask,
        CalendarTasksAdapter.TasksViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding = ItemTaskCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val currentItem = getItem(position)
        val nextItem: ExtendedTask? = if (position + 1 < itemCount) { getItem(position + 1) } else { null }
        holder.bind(currentItem, nextItem, position)
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

        fun bind(extendedTask: ExtendedTask, nextTask: ExtendedTask?, position: Int) {
            binding.apply {
                checkBoxCompleted.isChecked = extendedTask.completed
                textViewName.text = extendedTask.taskName
                textViewName.paint.isStrikeThruText = extendedTask.completed
                if (extendedTask.priority == 4) {
                    icPriority.visibility = View.GONE
                } else {
                    val color = chooseColorPriority(extendedTask.priority)
                    icPriority.setBackgroundColor(Color.parseColor(color))
                }

                if (extendedTask.timeStart != "No time" && extendedTask.timeEnd != "No time") {
                    val layoutParams: ViewGroup.MarginLayoutParams = binding.cardTaskCalendar.layoutParams as ViewGroup.MarginLayoutParams
                    var space = 0
                    var height = extendedTask.timeEndInt - extendedTask.timeStartInt
                    if (nextTask != null) {
                        space = nextTask.timeStartInt - extendedTask.timeEndInt
                        if (space == 0) {
                            space = 2
                            height -= 4
                        }
                    }
                    layoutParams.bottomMargin = convertDpToPx(space)
                    layoutParams.height = convertDpToPx(height)
                    if (position == 0 && extendedTask.timeStartInt > TIME_OFFSET) { layoutParams.topMargin = convertDpToPx(extendedTask.timeStartInt - TIME_OFFSET)}
                    binding.cardTaskCalendar.requestLayout()
                }
            }
        }

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
        fun onItemClick(extendedTask: ExtendedTask)
        fun onCheckBoxClick(extendedTask: ExtendedTask, isChecked: Boolean)
    }

    class DiffCallback : DiffUtil.ItemCallback<ExtendedTask>() {
        override fun areItemsTheSame(oldItem: ExtendedTask, newItem: ExtendedTask) =
            oldItem.taskId == newItem.taskId

        override fun areContentsTheSame(oldItem: ExtendedTask, newItem: ExtendedTask) =
            oldItem == newItem
    }
}