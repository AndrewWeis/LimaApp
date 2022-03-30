package start.up.tracker.analytics.principles

import start.up.tracker.analytics.entities.AnalyticsMessage
import start.up.tracker.analytics.holders.AnalyticsMessageHolder
import start.up.tracker.analytics.principles.base.Principle
import start.up.tracker.entities.Task
import start.up.tracker.utils.TimeHelper

class Pomodoro : Principle {

    override suspend fun checkComplianceOnAddTask(task: Task): AnalyticsMessage? {
        return checkComplianceToPrinciple(task)
    }

    override suspend fun checkComplianceOnEditTask(task: Task): AnalyticsMessage? {
        return checkComplianceToPrinciple(task)
    }

    /**
     * Первый, тестовый принцип
     * Добавление/редактирование новой таски:
     * Получаем список всех тасков, выполнение которых запланировано на день добавляемой/
     * редактируемой таски. Сканируем приоритетность этих тасков. 80% тасков не должны иметь
     * приоритета, 20% остальных тасков должны иметь приоритет (любой).
     * Для реагирования необходимо как минимум 3 таски.
     *
     * @param task активность
     */
    private fun checkComplianceToPrinciple(task: Task): AnalyticsMessage? {
        val currentDate = TimeHelper.getCurrentTimeInMilliseconds()
        val startDate = TimeHelper.computeStartDate(task)

        if (task.date == null || task.startTimeInMinutes == null || task.endTimeInMinutes == null) {
            AnalyticsMessageHolder.getPomodoroMessage()
        } else {
            // TODO write method
            if (currentDate >= startDate) {
                return null
            }
        }
        return null
    }
}
