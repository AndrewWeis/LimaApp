package start.up.tracker.analytics.principles.base

import start.up.tracker.analytics.entities.AnalyticsMessage
import start.up.tracker.entities.Task

interface Principle {
    fun checkCompatibility(activePrinciplesIds: List<Int>): Boolean
    suspend fun checkComplianceOnAddTask(task: Task): AnalyticsMessage?
    suspend fun checkComplianceOnEditTask(task: Task): AnalyticsMessage?
}
