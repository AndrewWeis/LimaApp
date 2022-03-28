package start.up.tracker.analytics.principles

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import start.up.tracker.R
import start.up.tracker.analytics.entities.AnalyticsMessage
import start.up.tracker.analytics.principles.base.Principle
import start.up.tracker.database.TechniquesIds
import start.up.tracker.database.TechniquesStorage
import start.up.tracker.database.dao.TaskAnalyticsDao
import start.up.tracker.entities.Task
import start.up.tracker.utils.resources.ResourcesUtils
import java.util.*
import javax.inject.Singleton

@Singleton
class Pareto(
    private val taskAnalyticsDao: TaskAnalyticsDao
) : Principle {

    private val incompatiblePrinciplesIds =
        TechniquesStorage.getIncompatiblePrinciplesIds(TechniquesIds.PARETO)

    override fun checkCompatibility(activePrinciplesIds: List<Int>): Boolean {
        for (principleId in activePrinciplesIds) {
            if (incompatiblePrinciplesIds.contains(principleId)) {
                return false
            }
        }
        return true
    }

    override suspend fun checkComplianceOnAddTask(task: Task): AnalyticsMessage? =
        withContext(Dispatchers.Default) {
            if (task.date != null) {
                val tasksOfDay = ArrayList(taskAnalyticsDao.getTasksOfDay(task.date))
                logicAddEditTask(task, tasksOfDay)
            }
            null
        }

    override suspend fun checkComplianceOnEditTask(task: Task): AnalyticsMessage? =
        withContext(Dispatchers.Default) {
            if (task.date != null) {
                val tasksOfDay = ArrayList(taskAnalyticsDao.getTasksOfDay(task.date))
                for (taskOfDay in tasksOfDay) {
                    if (taskOfDay.taskId == task.taskId) {
                        tasksOfDay.remove(taskOfDay)
                        break
                    }
                }
                logicAddEditTask(task, tasksOfDay)
            }
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
     * @param tasksOfDay список всех активностей дня, который имеет task
     */
    private fun logicAddEditTask(task: Task, tasksOfDay: ArrayList<Task>): AnalyticsMessage? {
        var priorityCount = 0

        for (taskOfDay in tasksOfDay) {
            if (taskOfDay.priority > 0) {
                priorityCount++
            }
        }

        if (task.priority > 0) {
            priorityCount++
        }

        if (tasksOfDay.size in 3..8 && priorityCount > 2) {
            return AnalyticsMessage(
                principleId = TechniquesIds.PARETO,
                title = ResourcesUtils.getString(R.string.pareto_message_title),
                message = ResourcesUtils.getString(
                    R.string.pareto_message_body,
                    tasksOfDay.size,
                    priorityCount
                ),
                messageDetailed = ResourcesUtils.getString(R.string.pareto_message_detailed)
            )
        } else if (tasksOfDay.size > 8 && (priorityCount.toDouble() * 100 / tasksOfDay.size) > 0.2) {
            return AnalyticsMessage(
                principleId = TechniquesIds.PARETO,
                title = ResourcesUtils.getString(R.string.pareto_message_title),
                message = ResourcesUtils.getString(
                    R.string.pareto_message_body,
                    tasksOfDay.size,
                    priorityCount
                ),
                messageDetailed = ResourcesUtils.getString(R.string.pareto_message_detailed)
            )
        }

        return null
    }
}
