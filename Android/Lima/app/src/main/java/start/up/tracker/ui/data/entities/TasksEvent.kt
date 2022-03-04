package start.up.tracker.ui.data.entities

import start.up.tracker.data.entities.ExtendedTask
import start.up.tracker.data.entities.Task

sealed class TasksEvent {
    object NavigateToAddTaskScreen : TasksEvent()
    data class ShowTaskSavedConfirmationMessage(val msg: String) : TasksEvent()
    object NavigateToDeleteAllCompletedScreen : TasksEvent()

    data class NavigateToEditExtendedTaskScreen(val extendedTask: ExtendedTask) : TasksEvent()
    data class NavigateToEditTaskScreen(val task: Task) : TasksEvent()

    data class ShowUndoDeleteExtendedTaskMessage(val extendedTask: ExtendedTask) : TasksEvent()
    data class ShowUndoDeleteTaskMessage(val task: Task) : TasksEvent()
}
