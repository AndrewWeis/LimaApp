package start.up.tracker.analytics.principles

import start.up.tracker.analytics.entities.AnalyticsMessage
import start.up.tracker.analytics.principles.base.Principle
import start.up.tracker.entities.Task

class EisenhowerMatrix : Principle {

    private val isOverridesPriority = true

    override suspend fun validateOnAddTask(task: Task): AnalyticsMessage? {
        // todo (implement)
        return null
    }

    override suspend fun validateOnEditTask(task: Task): AnalyticsMessage? {
        // todo (implement)
        return null
    }

    override suspend fun getIsOverridesPriority(): Boolean {
        return isOverridesPriority
    }
}
