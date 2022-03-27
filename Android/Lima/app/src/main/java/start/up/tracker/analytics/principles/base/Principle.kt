package start.up.tracker.analytics.principles.base

import start.up.tracker.analytics.entities.AnalyticsMessage
import start.up.tracker.entities.Task
import java.util.*

interface Principle {
    fun setStatus(status: Boolean)
    fun canBeEnabled(activePrinciplesIds: List<Int>): Boolean
    fun getStatus(): Boolean
    fun setNotifications(notifications: Boolean)
    fun getNotifications(): Boolean
    fun getName(): String?
    fun getId(): Int?
    fun getIncompatiblePrinciplesIds(): ArrayList<Int>?
    suspend fun logicAddTask(task: Task) : AnalyticsMessage?
    suspend fun logicEditTask(task: Task) : AnalyticsMessage?
}