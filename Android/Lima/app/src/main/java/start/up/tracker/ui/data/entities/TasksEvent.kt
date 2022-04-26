package start.up.tracker.ui.data.entities

import start.up.tracker.analytics.entities.AnalyticsMessages
import start.up.tracker.entities.Task

sealed class TasksEvent {
    object NavigateToAddTaskScreen : TasksEvent()
    object NavigateBack : TasksEvent()
    object NavigateToDeleteAllCompletedScreen : TasksEvent()
    data class NavigateToEditTaskScreen(val task: Task) : TasksEvent()
    data class ShowUndoDeleteTaskMessage(val task: Task, val subtasks: List<Task>) : TasksEvent()
    data class ShowAnalyticMessageDialog(val messages: AnalyticsMessages) : TasksEvent()

    data class NavigateToPriorityDialog(val priorityId: Int) : TasksEvent()
    data class ShowDatePicker(val date: Long?) : TasksEvent()
    data class ShowTimeStartPicker(val timeStart: Int?) : TasksEvent()
    data class ShowTimeEndPicker(val timeEnd: Int?) : TasksEvent()
    data class NavigateToProjectsDialog(val projectId: Int) : TasksEvent()
    data class NavigateToPomodoroDialog(val pomodoros: Int?, val startTime: Int?) : TasksEvent()
    data class NavigateToEisenhowerMatrixDialog(val optionId: Int) : TasksEvent()
    data class ShowError(val error: String) : TasksEvent()
}
