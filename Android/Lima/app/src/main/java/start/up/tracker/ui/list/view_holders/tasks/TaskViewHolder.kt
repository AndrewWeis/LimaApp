package start.up.tracker.ui.list.view_holders.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import start.up.tracker.R
import start.up.tracker.entities.Task
import start.up.tracker.ui.data.entities.forms.ListItem
import start.up.tracker.ui.data.entities.forms.Settings
import start.up.tracker.ui.list.view_holders.OnTaskClickListener
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder

open class TaskViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup,
    @LayoutRes contentLayoutId: Int = R.layout.task_item
) : BaseViewHolder(layoutInflater, parent, contentLayoutId) {

    private lateinit var settings: Settings
    private lateinit var task: Task
    private lateinit var listener: OnTaskClickListener

    private lateinit var taskCheckBox: CheckBox
    private lateinit var taskTitleText: TextView
    private lateinit var priorityImage: ImageView

    open fun bind(listItem: ListItem, listener: OnTaskClickListener) {
        this.task = listItem.data as Task
        this.settings = listItem.settings
        this.listener = listener

        initViews()

        setPriority()
        setCheckbox()
        setTaskTitle()

        setTaskClickListener()
        setCheckboxClickListener()
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
        taskTitleText.text = task.title
        taskTitleText.paint.isStrikeThruText = task.completed
    }

    private fun setCheckbox() {
        taskCheckBox.isChecked = task.completed
    }

    private fun setPriority() {
        if (task.priority == NO_PRIORITY) {
            priorityImage.visibility = View.GONE
        } else {
            priorityImage.setImageResource(
                getIconDrawableByPriority(task.priority)
            )
        }
    }

    private fun getIconDrawableByPriority(priority: Int): Int {
        return when (priority) {
            PRIORITY_HIGH -> R.drawable.ic_priority_1
            PRIORITY_MEDIUM -> R.drawable.ic_priority_2
            PRIORITY_LOW -> R.drawable.ic_priority_3
            else -> NOT_FOUND
        }
    }

    private companion object {
        const val NO_PRIORITY = 0
        const val PRIORITY_HIGH = 1
        const val PRIORITY_MEDIUM = 2
        const val PRIORITY_LOW = 3

        const val NOT_FOUND = -1
    }
}
