package start.up.tracker.analytics.principles

import start.up.tracker.analytics.entities.AnalyticsMessage
import start.up.tracker.analytics.principles.base.Principle
import start.up.tracker.database.TechniquesIds
import start.up.tracker.database.TechniquesStorage
import start.up.tracker.database.dao.TaskAnalyticsDao
import start.up.tracker.entities.Task
import javax.inject.Inject

class EisenhowerMatrix @Inject constructor(
    private val taskAnalyticsDao: TaskAnalyticsDao
) : Principle {

    private val incompatiblePrinciplesIds =
        TechniquesStorage.getIncompatiblePrinciplesIds(TechniquesIds.EISENHOWER_MATRIX)

    override fun canBeEnabled(activePrinciplesIds: List<Int>): Boolean {
        for (principleId in activePrinciplesIds) {
            if (incompatiblePrinciplesIds.contains(principleId)) {
                return false
            }
        }
        return true
    }

    override suspend fun logicAddTask(task: Task): AnalyticsMessage? {
        TODO("Not yet implemented")
    }

    override suspend fun logicEditTask(task: Task): AnalyticsMessage? {
        TODO("Not yet implemented")
    }
}
