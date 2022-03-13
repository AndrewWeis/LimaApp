package start.up.tracker.ui.list.view_holders

import start.up.tracker.entities.Task

interface OnTaskClickListener {
    fun onTaskClick(task: Task)
    fun onCheckBoxClick(task: Task)
}
