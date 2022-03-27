package start.up.tracker.analytics.principles.base

import start.up.tracker.analytics.entities.AnalyticsMessage
import start.up.tracker.entities.Task

interface Principle {
    fun canBeEnabled(activePrinciplesIds: List<Int>): Boolean
    suspend fun logicAddTask(task: Task): AnalyticsMessage?
    suspend fun logicEditTask(task: Task): AnalyticsMessage?
}
