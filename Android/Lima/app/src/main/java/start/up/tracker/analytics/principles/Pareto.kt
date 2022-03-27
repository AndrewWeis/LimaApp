package start.up.tracker.analytics.principles

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import start.up.tracker.analytics.entities.AnalyticsMessage
import start.up.tracker.analytics.principles.base.Principle
import start.up.tracker.database.dao.TaskAnalyticsDao
import start.up.tracker.entities.Task
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Pareto @Inject constructor(var taskAnalyticsDao: TaskAnalyticsDao) : Principle {
    private val id = 1  // пока указываем конкретный id
    private val name = "Pareto"
    private val incompatiblePrinciplesIds = arrayListOf(2)

    private var status: Boolean = false
    private var notifications: Boolean = false

    /**
     * Меняем статус от вкл к выкл всегда,
     * а статус от выкл к вкл только тогда, когда активные принципы совместимы с включаемым
     * принципом
     */
    override fun setStatus(status: Boolean) {
        this.status = status
    }

    override fun canBeEnabled(activePrinciplesIds: List<Int>): Boolean {
        for (principleId in activePrinciplesIds) {
            if (incompatiblePrinciplesIds.contains(principleId)) {
                return false
            }
        }
        return true
    }

    override fun getStatus(): Boolean {
        return status
    }

    override fun setNotifications(notifications: Boolean) {
        this.notifications = notifications
    }

    override fun getNotifications(): Boolean {
        return notifications
    }

    override fun getName(): String {
        return name
    }

    override fun getId(): Int {
        return id
    }

    override fun getIncompatiblePrinciplesIds(): ArrayList<Int> {
        return incompatiblePrinciplesIds
    }

    override suspend fun logicAddTask(task: Task): AnalyticsMessage? =
        withContext(Dispatchers.Default) {
            if (task.date != null) {
                val tasksOfDay = ArrayList(taskAnalyticsDao.getTasksOfDay(task.date))
                logicAddEditTask(task, tasksOfDay)
            }
            null
        }

    override suspend fun logicEditTask(task: Task): AnalyticsMessage? =
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
            /* TODO Андрей. диалоговое окно о том, что мы нарушаем принцип. 2 кнопки:
            TODO продолжить и отменить, развернуть сообщение целиком и перейти к статье */
            return AnalyticsMessage(id,"Pareto principle is violated",
                "Pareto principle suggests that you should prioritize 20% of daily tasks" +
                        "over 80% other tasks while you have " + tasksOfDay.size + " tasks and " +
                        priorityCount + " of them are prioritized",
                "Try priories your tasks in a way that the principle suggests"
            )
        } else if (tasksOfDay.size > 8 &&
            (priorityCount.toDouble() * 100 / tasksOfDay.size) > 0.2
        ) {
            /* TODO Андрей. диалоговое окно о том, что мы нарушаем принцип. 2 кнопки:
            TODO продолжить и отменить, развернуть сообщение целиком и перейти к статье */
            return AnalyticsMessage(id, "Pareto principle is violated",
                "Pareto principle suggests that you should prioritize 20% of daily tasks" +
                        "over 80% other tasks while you have " + tasksOfDay.size + " tasks and " +
                        priorityCount + " of them are prioritized",
                "Try priories your tasks in a way that the principle suggests"
            )
        }
        return null
    }
}