package start.up.tracker.analytics.principles

import start.up.tracker.analytics.entities.AnalyticsMessage
import start.up.tracker.analytics.principles.base.Principle
import start.up.tracker.database.dao.TaskAnalyticsDao
import start.up.tracker.entities.Task
import java.util.*
import javax.inject.Inject

class EisenhowerMatrix @Inject constructor(var taskAnalyticsDao: TaskAnalyticsDao) : Principle {

    private val id = 2
    private val name = "Eisenhower Matrix"
    private val incompatiblePrinciplesIds = arrayListOf(1)

    private var status: Boolean = false
    private var notifications: Boolean = false

    override fun setStatus(status: Boolean) {
        this.status = status
    }

    override fun canBeEnabled(activePrinciplesIds: List<Int>): Boolean {
        for (principleId in activePrinciplesIds) {
            if (incompatiblePrinciplesIds.contains(principleId)) {
                return false
            }
        }
        return true
    }

    override fun getStatus(): Boolean {
        return status
    }

    override fun setNotifications(notifications: Boolean) {
        this.notifications = notifications
    }

    override fun getNotifications(): Boolean {
        return notifications
    }

    override fun getName(): String {
        return name
    }

    override fun getId(): Int {
        return id
    }

    override fun getIncompatiblePrinciplesIds(): ArrayList<Int> {
        return incompatiblePrinciplesIds
    }

    override suspend fun logicAddTask(task: Task): AnalyticsMessage? {
        TODO("Not yet implemented")
    }

    override suspend fun logicEditTask(task: Task): AnalyticsMessage? {
        TODO("Not yet implemented")
    }
}