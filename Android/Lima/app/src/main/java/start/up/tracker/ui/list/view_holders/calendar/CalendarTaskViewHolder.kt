package start.up.tracker.ui.list.view_holders.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.CalendarTaskItemBinding
import start.up.tracker.entities.Task
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.list.view_holders.tasks.OnTaskClickListener
import start.up.tracker.ui.list.view_holders.tasks.base.BaseTaskViewHolder
import start.up.tracker.utils.resources.ResourcesUtils

class CalendarTaskViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup
) : BaseTaskViewHolder(layoutInflater, parent, R.layout.calendar_task_item) {

    private var binding: CalendarTaskItemBinding = CalendarTaskItemBinding.bind(itemView)

    fun bind(
        listItem: ListItem,
        nextListItem: ListItem?,
        position: Int,
        listener: OnTaskClickListener
    ) {
        super.bind(listItem, listener)

        val nextTask: Task? = nextListItem?.data as Task?
        setTaskCard(nextTask, position)

        setPriorityImageBackground()
    }

    private fun setTaskCard(nextTask: Task?, position: Int) {
        if (task.startTimeInMinutes == null || task.endTimeInMinutes == null) {
            return
        }

        val layoutParams = binding.cardTaskCalendar.layoutParams as ViewGroup.MarginLayoutParams
        val cardHeight = task.endTimeInMinutes!! - task.startTimeInMinutes!!

        // разница между нижней границей текущего таска и верхней границей следующего
        var bottomMargin = 0
        nextTask?.let {
            bottomMargin = it.startTimeInMinutes!! - task.endTimeInMinutes!!
        }

        layoutParams.bottomMargin = ResourcesUtils.getPxByDp(bottomMargin + 2F)
        layoutParams.height = ResourcesUtils.getPxByDp(cardHeight - 2F)

        if (position == 0) {
            layoutParams.topMargin = ResourcesUtils.getPxByDp((task.startTimeInMinutes!!.toFloat()))
        }

        binding.cardTaskCalendar.requestLayout()
    }
}
