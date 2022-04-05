package start.up.tracker.analytics.principles

import start.up.tracker.analytics.entities.AnalyticsMessage
import start.up.tracker.analytics.holders.AnalyticsMessageHolder
import start.up.tracker.analytics.principles.base.Principle
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.entities.Task
import start.up.tracker.utils.TimeHelper
import java.lang.RuntimeException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Pomodoro(
    private val taskDao: TaskDao,
) : Principle {


    override suspend fun checkComplianceOnAddTask(task: Task): AnalyticsMessage? {
        return checkComplianceToPrinciple(task)
    }

    override suspend fun checkComplianceOnEditTask(task: Task): AnalyticsMessage? {
        return checkComplianceToPrinciple(task)
    }

    private fun checkComplianceToPrinciple(task: Task): AnalyticsMessage? {
        return null
    }

    /**
     * Метод находит активность, которая на текущий момент идёт или начнётся через 5 минут
     */
    suspend fun findTaskToMatch(): Task? {
        val currentDay = TimeHelper.getCurrentDayInMilliseconds()
        val tasksOfDay = taskDao.getTasksOfDay(currentDay)

        var min = 60 * 24
        var minTask: Task? = null

        for (taskOfDay in tasksOfDay) {
            if (taskOfDay.startTimeInMinutes == null) {
                continue
            }
            if (taskOfDay.startTimeInMinutes < min && fromEndTimeToPomodoro(taskOfDay) != null &&
                TimeHelper.isTaskUnderway(taskOfDay, 300000, 0)
            ) {
                min = taskOfDay.startTimeInMinutes
                minTask = taskOfDay
            }
        }

        return minTask
    }

    // TODO Андрею при записи в бд
    fun fromPomodoroToEndTime(task: Task, pomodoroNumber: Int): Int? {
        // TODO на стороне Андрея убеждаться или кидать Toast, что помидорки могут выставляться
        // TODO только совместно временем начала активности
        if (task.startTimeInMinutes == null || pomodoroNumber == 0) {
            return null
        }

        return task.startTimeInMinutes + pomodoroNumber * 25 + (pomodoroNumber - 1) * 5 +
                (pomodoroNumber - 1) / 4 * 15
    }

    // TODO Андрею при получении из бд
    fun fromEndTimeToPomodoro(task: Task): Int? {
        if (task.startTimeInMinutes == null || task.endTimeInMinutes == null) {
            return null
        }

        var c = task.endTimeInMinutes - task.startTimeInMinutes
        var pomCounter = 0

        while (c > 0) {
            pomCounter++
            c -= 25

            if (c != 0) {
                c -= if (pomCounter % 4 == 0)
                    20
                else
                    5
            }
        }

        return pomCounter
    }

    fun fromEndTimeToPomodoroTest(task: Task, pomodoroNumber: Int): Int? {

        if (task.startTimeInMinutes == null || pomodoroNumber == 0) {
            return null
        }

        val b = task.startTimeInMinutes + pomodoroNumber * 25 + (pomodoroNumber - 1) * 5 +
                (pomodoroNumber - 1) / 4 * 15

        var c = b - task.startTimeInMinutes

        var pomCounter = 0
        while (c > 0) {
            pomCounter++
            c -= 25

            if (c != 0) {
                c -= if (pomCounter % 4 == 0)
                    20
                else
                    5
            }
        }

        return pomCounter
    }
}
