package start.up.tracker.analytics

import start.up.tracker.entities.Task
import java.util.ArrayList

interface Principle {
    fun setStatus(status: Boolean)
    fun canBeEnabled(activePrinciples: List<Principle>): Boolean
    fun getStatus(): Boolean
    fun setNotifications(notifications: Boolean)
    fun getNotifications(): Boolean
    fun getTimeToRead(): Int?
    fun getReference(): String?
    fun getName(): String?
    fun getIncompatiblePrinciples(): ArrayList<Principle>?
    fun logic()
    suspend fun logicAddTask(task: Task)
    suspend fun logicEditTask(task: Task)
}