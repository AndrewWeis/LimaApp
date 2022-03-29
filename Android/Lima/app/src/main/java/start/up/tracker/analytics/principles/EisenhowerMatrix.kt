package start.up.tracker.analytics.principles

import start.up.tracker.analytics.entities.AnalyticsMessage
import start.up.tracker.analytics.principles.base.Principle
import start.up.tracker.database.TechniquesIds
import start.up.tracker.database.TechniquesStorage
import start.up.tracker.database.dao.TaskAnalyticsDao
import start.up.tracker.entities.Task

class EisenhowerMatrix(
    private val taskAnalyticsDao: TaskAnalyticsDao
) : Principle {

    private val incompatiblePrinciplesIds =
        TechniquesStorage.getIncompatiblePrinciplesIds(TechniquesIds.EISENHOWER_MATRIX)

    override fun checkCompatibility(activePrinciplesIds: List<Int>): Boolean {
        for (principleId in activePrinciplesIds) {
            if (incompatiblePrinciplesIds.contains(principleId)) {
                return false
            }
        }
        return true
    }

    override suspend fun checkComplianceOnAddTask(task: Task): AnalyticsMessage? {
        TODO("Not yet implemented")
    }

    override suspend fun checkComplianceOnEditTask(task: Task): AnalyticsMessage? {
        TODO("Not yet implemented")
    }
}
