package start.up.tracker.analytics.principles

import start.up.tracker.R
import start.up.tracker.analytics.entities.AnalyticsMessage
import start.up.tracker.analytics.principles.base.Principle
import start.up.tracker.database.TechniquesIds
import start.up.tracker.database.TechniquesStorage
import start.up.tracker.database.dao.TaskAnalyticsDao
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.entities.Task
import start.up.tracker.utils.resources.ResourcesUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Pareto(
    private val taskAnalyticsDao: TaskAnalyticsDao
) : Principle {

    @Inject
    private lateinit var taskDao: TaskDao

    private val incompatiblePrinciplesIds =
        TechniquesStorage.getIncompatiblePrinciplesIds(TechniquesIds.PARETO)

    override fun checkCompatibility(activePrinciplesIds: List<Int>): Boolean {
        activePrinciplesIds.forEach {
            if (incompatiblePrinciplesIds.contains(it)) {
                return false
            }
        }
        return true
    }

    override suspend fun checkComplianceOnAddTask(task: Task): AnalyticsMessage? {
        return task.date?.let { date ->
            val tasksOfDay = taskDao.getTasksOfDay(date)
            logicAddEditTask(task, tasksOfDay)
        }
    }

    override suspend fun checkComplianceOnEditTask(task: Task): AnalyticsMessage? {
        return task.date?.let { date ->
            // Так как этот метод вызывается до того как мы обновили базу данных, то при редактировании
            // сегодняшний даты на другую, нам нужно не учитывать эту таску
            val tasksOfDay = taskDao.getTasksOfDay(date)
                .filter { taskOfDay ->
                    taskOfDay.taskId != task.taskId
                }

            logicAddEditTask(task, tasksOfDay)
        }
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
    private fun logicAddEditTask(task: Task, tasksOfDay: List<Task>): AnalyticsMessage? {
        // подсчитываем количество выставленных приоритетов у задач на сегодня
        var priorityCount = tasksOfDay.count { task.priority > 0 }

        // + учитываем приоритет добавляемой / редактируемой задачи
        if (task.priority > 0) {
            priorityCount++
        }

        return if (tasksOfDay.size in 3..8 && priorityCount > 2) {
            AnalyticsMessage(
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
            AnalyticsMessage(
                principleId = TechniquesIds.PARETO,
                title = ResourcesUtils.getString(R.string.pareto_message_title),
                message = ResourcesUtils.getString(
                    R.string.pareto_message_body,
                    tasksOfDay.size,
                    priorityCount
                ),
                messageDetailed = ResourcesUtils.getString(R.string.pareto_message_detailed)
            )
        } else {
            null
        }
    }
}
