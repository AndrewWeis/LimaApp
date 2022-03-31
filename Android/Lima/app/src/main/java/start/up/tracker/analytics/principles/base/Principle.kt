package start.up.tracker.analytics.principles.base

import start.up.tracker.R
import start.up.tracker.analytics.entities.AnalyticsMessage
import start.up.tracker.analytics.holders.AnalyticsMessageHolder
import start.up.tracker.database.TechniquesStorage
import start.up.tracker.entities.Task
import start.up.tracker.utils.resources.ResourcesUtils

interface Principle {

    suspend fun checkComplianceOnAddTask(task: Task): AnalyticsMessage?
    suspend fun checkComplianceOnEditTask(task: Task): AnalyticsMessage?

    suspend fun checkCompatibility(
        activePrinciplesIds: List<Int>,
        techniqueId: Int
    ): AnalyticsMessage? {

        val incompatiblePrinciplesIds =
            TechniquesStorage.getIncompatiblePrinciplesIds(techniqueId)

        val currentPrincipleName = AnalyticsMessageHolder.getPrincipleNameById(techniqueId)

        var error = ""
        for (principleId in activePrinciplesIds) {
            if (incompatiblePrinciplesIds.contains(principleId)) {
                error += AnalyticsMessageHolder.getPrincipleNameById(principleId) + "\n"
            }
        }

        if (error.isEmpty()) {
            return null
        }

        return AnalyticsMessage(
            principleId = techniqueId,
            title = ResourcesUtils.getString(R.string.incompatible_message_title, currentPrincipleName),
            error = error,
            hint = ResourcesUtils.getString(R.string.incompatible_message_hint)
        )
    }
}
