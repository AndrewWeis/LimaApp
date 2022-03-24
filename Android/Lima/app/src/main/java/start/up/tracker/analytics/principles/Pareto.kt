package start.up.tracker.analytics.principles

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import start.up.tracker.R
import start.up.tracker.analytics.Principle
import start.up.tracker.database.dao.AnalyticsDao
import start.up.tracker.database.dao.TaskAnalyticsDao
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.entities.Task
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Pareto @Inject constructor(var taskAnalyticsDao: TaskAnalyticsDao) : Principle {
    private val name = "Pareto"
    private val timeToRead = 2
    private val reference = "www.bbc.com"
    private val incompatiblePrinciples = ArrayList<Principle>()

    private var status: Boolean = false
    private var notifications: Boolean = false

    /**
     * Меняем статус от вкл к выкл всегда,
     * а статус от выкл к вкл только тогда, когда активные принципы совместимы с включаемым
     * принципом
     */
    override fun setStatus(status: Boolean) {
        /*if (boolean) {
            for (principle in activePrinciples) {
                if (incompatiblePrinciples.contains(principle)) {
                    Toast.makeText(applicationContext,
                        "Cannot enable the principle because " + principle +
                                " is already enabled",
                        Toast.LENGTH_SHORT).show()
                    status = true
                }
            }
            status = false
        } else {
            status = boolean
        }*/
        this.status = status
    }

    override fun canBeEnabled(activePrinciples: List<Principle>): Boolean {
        for (principle in activePrinciples) {
            if (incompatiblePrinciples.contains(principle)) {
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

    override fun getTimeToRead(): Int {
        return timeToRead
    }

    override fun getReference(): String {
        return reference
    }

    override fun getIncompatiblePrinciples(): ArrayList<Principle> {
        return incompatiblePrinciples
    }

    override fun logic() {
        TODO("Not yet implemented")
    }

    override suspend fun logicAddTask(task: Task) = withContext(Dispatchers.Default) {
        if (task.date != null) {
            val tasksOfDay = ArrayList(taskAnalyticsDao.getTasksOfDay(task.date))
            logicAddEditTask(task, tasksOfDay)
        }
    }

    override suspend fun logicEditTask(task: Task) = withContext(Dispatchers.Default) {
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
    }

    /**
     * Первый, тестовый принцип
     * Добавление/редактирование новой таски:
     * Получаем список всех тасков, выполнение которых запланировано на день добавляемой/
     * редактируемой таски. Сканируем приоритетность этих тасков. 80% тасков не должны иметь
     * приоритета, 20% остальных тасков должны иметь приоритет (любой).
     * Для реагирования необходимо как минимум 3 таски.
     */
    private fun logicAddEditTask(task: Task, tasksOfDay: ArrayList<Task>) {
        var priorityCount = 0
        for (taskOfDay in tasksOfDay) {
            if (taskOfDay.priority > 0) {
                priorityCount++
            }
        }
        if (tasksOfDay.size == 3 && priorityCount > 1) {
                /* TODO диалоговое окно о том, что мы нарушаем принцип. 2 кнопки:
                TODO продолжить и отменить */
            // Я тут отправляю сигнал о необходимости создания диалог окна
        } else if (tasksOfDay.size in 3..8 && priorityCount > 2) {
                /* TODO диалоговое окно о том, что мы нарушаем принцип. 2 кнопки:
                TODO продолжить и отменить */
            // Я тут отправляю сигнал о необходимости создания диалог окна
        } else if (tasksOfDay.size > 8 &&
            (priorityCount.toDouble() * 100 / tasksOfDay.size) > 0.2) {
                /* TODO диалоговое окно о том, что мы нарушаем принцип. 2 кнопки:
                TODO продолжить и отменить */
            // Я тут отправляю сигнал о необходимости создания диалог окна
        }
    }

    /*class MyDialogFragment : DialogFragment() {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.setTitle("Важное сообщение!")
                    .setMessage("Покормите кота!")
                    .setPositiveButton("ОК, иду на кухню") {
                            dialog, id ->  dialog.cancel()
                    }
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }*/
}