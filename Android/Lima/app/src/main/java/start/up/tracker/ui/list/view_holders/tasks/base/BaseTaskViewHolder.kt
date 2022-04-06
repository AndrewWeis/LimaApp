package start.up.tracker.ui.list.view_holders.tasks.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import start.up.tracker.R
import start.up.tracker.entities.Task
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.ui.list.view_holders.tasks.OnTaskClickListener
import start.up.tracker.utils.resources.ResourcesUtils

abstract class BaseTaskViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup,
    @LayoutRes contentLayoutId: Int = R.layout.task_item
) : BaseViewHolder(layoutInflater, parent, contentLayoutId) {

    protected lateinit var task: Task
    protected lateinit var listener: OnTaskClickListener

    private lateinit var taskCheckBox: CheckBox
    private lateinit var taskTitleText: TextView
    private lateinit var priorityImage: ImageView

    open fun bind(listItem: ListItem, listener: OnTaskClickListener) {
        this.task = listItem.data as Task
        this.listener = listener

        initViews()

        setPriority()
        setCheckbox()
        setTaskTitle()

        setTaskClickListener()
        setCheckboxClickListener()
    }

    protected fun setPriorityImageBackground() {
        val colorRes = ResourcesUtils.getColor(getPriorityColorByPriority(task.priority))
        priorityImage.setBackgroundColor(colorRes)
    }

    private fun initViews() {
        taskCheckBox = itemView.findViewById(R.id.task_check_box)
        taskTitleText = itemView.findViewById(R.id.task_title_text)
        priorityImage = itemView.findViewById(R.id.priority_image)
    }

    private fun setCheckboxClickListener() {
        taskCheckBox.setOnClickListener {
            listener.onCheckBoxClick(task.copy(completed = taskCheckBox.isChecked))
        }
    }

    private fun setTaskClickListener() {
        itemView.setOnClickListener {
            listener.onTaskClick(task)
        }
    }

    private fun setTaskTitle() {
        taskTitleText.text = task.taskTitle
        taskTitleText.paint.isStrikeThruText = task.completed
    }

    private fun setCheckbox() {
        taskCheckBox.isChecked = task.completed
    }

    private fun setPriority() {
        if (task.priority == Task.NO_PRIORITY) {
            priorityImage.visibility = View.GONE
            return
        }

        val imageRes = getPriorityImageByPriority(task.priority)
        priorityImage.setImageResource(imageRes)

        val colorRes = ResourcesUtils.getColor(getPriorityColorByPriority(task.priority))
        priorityImage.setColorFilter(colorRes)
    }

    private fun getPriorityColorByPriority(priority: Int): Int {
        return when (priority) {
            Task.PRIORITY_HIGH -> R.color.priority_high
            Task.PRIORITY_MEDIUM -> R.color.priority_medium
            Task.PRIORITY_LOW -> R.color.priority_low
            else -> android.R.color.transparent
        }
    }

    private fun getPriorityImageByPriority(priority: Int): Int {
        return when (priority) {
            Task.PRIORITY_HIGH -> R.drawable.ic_priority_fire_3
            Task.PRIORITY_MEDIUM -> R.drawable.ic_priority_fire_2
            Task.PRIORITY_LOW -> R.drawable.ic_priority_fire_1
            else -> NOT_FOUND
        }
    }

    private companion object {
        const val NOT_FOUND = 0
    }
}
