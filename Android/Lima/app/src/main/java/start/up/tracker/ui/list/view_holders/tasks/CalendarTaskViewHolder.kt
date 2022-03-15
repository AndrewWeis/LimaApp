package start.up.tracker.ui.list.view_holders.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.CalendarTaskItemBinding
import start.up.tracker.entities.Task
import start.up.tracker.ui.data.constants.TIME_OFFSET
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.Settings
import start.up.tracker.utils.resources.ResourcesUtils

class CalendarTaskViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup
) : TaskViewHolder(layoutInflater, parent, R.layout.calendar_task_item) {

    private var binding: CalendarTaskItemBinding = CalendarTaskItemBinding.bind(itemView)

    private lateinit var settings: Settings
    private lateinit var task: Task

    fun bind(
        listItem: ListItem,
        nextListItem: ListItem?,
        position: Int,
        listener: OnTaskClickListener
    ) {
        super.bind(listItem, listener)
        this.task = listItem.data as Task
        this.settings = listItem.settings

        val nextTask: Task? = nextListItem?.data as Task?
        setTaskCard(nextTask, position)

        setPriorityImageBackground()
    }

    private fun setTaskCard(nextTask: Task?, position: Int) {
        if (task.startTimeInMinutes == null || task.endTimeInMinutes == null) {
            return
        }

        val layoutParams = binding.cardTaskCalendar.layoutParams as ViewGroup.MarginLayoutParams
        var cardHeight = task.endTimeInMinutes!! - task.startTimeInMinutes!!

        // разница между нижней границей текущего таска и верхней границей следующего
        var space = 0
        nextTask?.let {
            space = it.startTimeInMinutes?.minus(task.endTimeInMinutes!!) ?: 0
            if (space == 0) {
                space = 2
                cardHeight -= 4
            }
        }

        layoutParams.bottomMargin = ResourcesUtils.getPxByDp(space.toFloat())
        layoutParams.height = ResourcesUtils.getPxByDp(cardHeight.toFloat())

        // у самого первого таска должен быть отступ сверху
        if (position == 0 && task.startTimeInMinutes!! > TIME_OFFSET) {
            layoutParams.topMargin =
                ResourcesUtils.getPxByDp((task.startTimeInMinutes!! - TIME_OFFSET))
        }

        binding.cardTaskCalendar.requestLayout()
    }
}
