package start.up.tracker.analytics.principles

import start.up.tracker.analytics.entities.AnalyticsMessage
import start.up.tracker.analytics.holders.AnalyticsMessageHolder
import start.up.tracker.analytics.principles.base.Principle
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.entities.Task
import javax.inject.Singleton

@Singleton
class Pareto(
    private val taskDao: TaskDao
) : Principle {

    override suspend fun checkComplianceOnAddTask(task: Task): AnalyticsMessage? {
        return task.date?.let { date ->
            val tasksOfDay = taskDao.getTasksOfDay(date)
            checkComplianceToPrinciple(task, tasksOfDay)
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

            checkComplianceToPrinciple(task, tasksOfDay)
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
    private fun checkComplianceToPrinciple(task: Task, tasksOfDay: List<Task>): AnalyticsMessage? {
        // подсчитываем количество выставленных приоритетов у задач на сегодня
        var priorityCount = tasksOfDay.count { task.priority > 0 }

        // + учитываем приоритет добавляемой / редактируемой задачи
        if (task.priority > 0) {
            priorityCount++
        }

        return if (tasksOfDay.size in 3..8 && priorityCount > 2) {
            AnalyticsMessageHolder.getParetoMessage(tasksOfDay.size, priorityCount)
        } else if (tasksOfDay.size > 8 && (priorityCount.toDouble() * 100 / tasksOfDay.size) > 0.2) {
            AnalyticsMessageHolder.getParetoMessage(tasksOfDay.size, priorityCount)
        } else {
            null
        }
    }
}
