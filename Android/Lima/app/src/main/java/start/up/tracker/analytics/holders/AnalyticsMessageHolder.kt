package start.up.tracker.analytics.holders

import start.up.tracker.R
import start.up.tracker.analytics.entities.AnalyticsMessage
import start.up.tracker.database.TechniquesIds
import start.up.tracker.utils.resources.ResourcesUtils

object AnalyticsMessageHolder {

    // todo (classify messages. Use Pattern [get][principle][classification])
    fun getParetoMessage(tasks: Int, priorityCount: Int): AnalyticsMessage {
        return AnalyticsMessage(
            principleId = TechniquesIds.PARETO,
            title = ResourcesUtils.getString(R.string.pareto_message_title),
            message = ResourcesUtils.getString(
                R.string.pareto_message_body,
                tasks,
                priorityCount
            ),
            messageDetailed = ResourcesUtils.getString(R.string.pareto_message_detailed)
        )
    }

    // todo (classify messages. Use Pattern [get][principle][classification])
    fun getPomodoroMessage(): AnalyticsMessage {
        return AnalyticsMessage(
            principleId = TechniquesIds.POMODORO,
            title = ResourcesUtils.getString(R.string.pomodoro_message_title),
            message = ResourcesUtils.getString(R.string.pomodoro_message_body),
            messageDetailed = ResourcesUtils.getString(R.string.pomodoro_message_detailed)
        )
    }
}
