package start.up.tracker.analytics.principles.base

import start.up.tracker.analytics.entities.AnalyticsMessage
import start.up.tracker.database.TechniquesStorage
import start.up.tracker.entities.Task

interface Principle {
    suspend fun checkCompatibility(activePrinciplesIds: List<Int>, techniqueId: Int): Boolean {
        val incompatiblePrinciplesIds =
            TechniquesStorage.getIncompatiblePrinciplesIds(techniqueId)

        for (principleId in activePrinciplesIds) {
            if (incompatiblePrinciplesIds.contains(principleId)) {
                return false
            }
        }

        return true
    }

    suspend fun checkComplianceOnAddTask(task: Task): AnalyticsMessage?
    suspend fun checkComplianceOnEditTask(task: Task): AnalyticsMessage?
}
