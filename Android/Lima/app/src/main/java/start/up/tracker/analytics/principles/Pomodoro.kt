package start.up.tracker.analytics.principles

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import start.up.tracker.analytics.entities.AnalyticsMessage
import start.up.tracker.analytics.principles.base.Principle
import start.up.tracker.database.TechniquesIds
import start.up.tracker.database.TechniquesStorage
import start.up.tracker.database.dao.TaskAnalyticsDao
import start.up.tracker.entities.Task
import start.up.tracker.utils.TimeHelper
import javax.inject.Inject

class Pomodoro @Inject constructor(
    private val taskAnalyticsDao: TaskAnalyticsDao
) : Principle {

    private val incompatiblePrinciplesIds =
        TechniquesStorage.getIncompatiblePrinciplesIds(TechniquesIds.POMODORO)

    override fun canBeEnabled(activePrinciplesIds: List<Int>): Boolean {
        for (principleId in activePrinciplesIds) {
            if (incompatiblePrinciplesIds.contains(principleId)) {
                return false
            }
        }
        return true
    }

    override suspend fun logicAddTask(task: Task): AnalyticsMessage? =
        withContext(Dispatchers.Default) {
            logicAddEditTask(task)
            null
        }

    override suspend fun logicEditTask(task: Task): AnalyticsMessage? =
        withContext(Dispatchers.Default) {
            logicAddEditTask(task)
            null
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
    private fun logicAddEditTask(task: Task): AnalyticsMessage? {
        val currentDate = TimeHelper.getCurrentTimeInMilliseconds()
        val startDate = TimeHelper.computeStartDate(task)
        if (task.date == null || task.startTimeInMinutes == null || task.endTimeInMinutes == null) {
            return AnalyticsMessage(
                TechniquesIds.POMODORO, "Pomodoro principle cannot be applied",
                "Pomodoro principle requires ",
                "Try priories your tasks in a way that the principle suggests"
            )
        } else {
            // TODO write method
            if (currentDate >= startDate) {
                return null
            }
        }
        return null
    }
}