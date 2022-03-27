package start.up.tracker.analytics.principles

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import start.up.tracker.analytics.Principle
import start.up.tracker.analytics.entities.AnalyticsMessage
import start.up.tracker.database.dao.TaskAnalyticsDao
import start.up.tracker.entities.Task
import start.up.tracker.utils.TimeHelper
import java.util.ArrayList
import javax.inject.Inject

class Pomodoro @Inject constructor(var taskAnalyticsDao: TaskAnalyticsDao) : Principle {
    private val id = 1  // пока указываем конкретный id
    private val name = "Pomodoro"
    private val incompatiblePrinciplesIds = arrayListOf(0)

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
            return AnalyticsMessage(id, "Pomodoro principle cannot be applied",
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