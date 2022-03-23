package start.up.tracker.analytics.principles

import start.up.tracker.analytics.Principle
import start.up.tracker.database.dao.TaskAnalyticsDao
import start.up.tracker.entities.Task
import java.util.ArrayList
import javax.inject.Inject

class EisenhowerMatrix @Inject constructor(var taskAnalyticsDao: TaskAnalyticsDao) : Principle {
    private val name = "Eisenhower Matrix"
    private val timeToRead = 2
    private val reference = "www.bbc.com"
    private val incompatiblePrinciples = ArrayList<Principle>()

    private var status: Boolean = false
    private var notifications: Boolean = false

    override fun setStatus(boolean: Boolean) {
        /*if (boolean) {
            for (principle in activePrinciples) {
                if (incompatiblePrinciples.contains(principle)) {
                    Toast.makeText(applicationContext,
                        "Cannot enable the principle because " + principle +
                                " is already enabled",
                        Toast.LENGTH_SHORT).show()
                    status = true
                }
            }
            status = false
        } else {
            status = boolean
        }*/
        status = boolean
    }

    override fun getStatus(): Boolean {
        return status
    }

    override fun setNotifications(boolean: Boolean) {
        notifications = boolean
    }

    override fun getNotifications(): Boolean {
        return notifications
    }

    override fun getName(): String {
        return name
    }

    override fun getTimeToRead(): Int {
        return timeToRead
    }

    override fun getReference(): String {
        return reference
    }

    override fun getIncompatiblePrinciples(): ArrayList<Principle> {
        return incompatiblePrinciples
    }

    override fun logic() {
        TODO("Not yet implemented")
    }

    override suspend fun logicAddTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun logicEditTask(task: Task) {
        TODO("Not yet implemented")
    }
}