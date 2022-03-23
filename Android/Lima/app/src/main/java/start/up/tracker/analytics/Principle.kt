package start.up.tracker.analytics

import start.up.tracker.entities.Task
import java.util.ArrayList

interface Principle {
    fun setStatus(boolean: Boolean)
    fun getStatus(): Boolean
    fun setNotifications(boolean: Boolean)
    fun getNotifications(): Boolean
    fun getTimeToRead(): Int?
    fun getReference(): String?
    fun getName(): String?
    fun getIncompatiblePrinciples(): ArrayList<Principle>?
    fun logic()
    suspend fun logicAddTask(task: Task)
    suspend fun logicEditTask(task: Task)
}