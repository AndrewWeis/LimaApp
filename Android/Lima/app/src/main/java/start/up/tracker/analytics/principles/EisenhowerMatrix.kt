package start.up.tracker.analytics.principles

import start.up.tracker.analytics.entities.AnalyticsMessage
import start.up.tracker.analytics.principles.base.Principle
import start.up.tracker.entities.Task

class EisenhowerMatrix : Principle {

    override suspend fun validateOnAddTask(task: Task): AnalyticsMessage? {
        TODO("Not yet implemented")
    }

    override suspend fun validateOnEditTask(task: Task): AnalyticsMessage? {
        TODO("Not yet implemented")
    }
}
