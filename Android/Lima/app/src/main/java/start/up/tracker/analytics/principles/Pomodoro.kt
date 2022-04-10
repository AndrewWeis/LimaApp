package start.up.tracker.analytics.principles

import start.up.tracker.analytics.entities.AnalyticsMessage
import start.up.tracker.analytics.principles.base.Principle
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.entities.Task
import start.up.tracker.utils.TimeHelper
import javax.inject.Singleton

@Singleton
class Pomodoro(
    private val taskDao: TaskDao,
) : Principle {

    private val isOverridesPriority = false

    override suspend fun validateOnAddTask(task: Task): AnalyticsMessage? {
        return validate(task)
    }

    override suspend fun validateOnEditTask(task: Task): AnalyticsMessage? {
        return validate(task)
    }

    override suspend fun getIsOverridesPriority(): Boolean {
        return isOverridesPriority
    }

    private fun validate(task: Task): AnalyticsMessage? {
        return null
    }

    /**
     * Возвращает задачи назначенные на сегодня в определенном порядке.
     * Сначала показывает задачи, у которых выставлено время.
     * Далее задачи на сегодня без времени, отсорированные по приоритету
     */
    suspend fun getClosestTasksOfToday(): List<Task> {
        val today = TimeHelper.getCurrentDayInMilliseconds()
        val todayInMinutes = TimeHelper.getMinutesOfCurrentDay()

        val tasksOfToday = taskDao.getTasksOfDay(today)

        // firstly we show tasks which have time start and time end
        val tasksWithTime = tasksOfToday
            .filter { task ->
                task.startTimeInMinutes != null &&
                task.startTimeInMinutes + 5 >= todayInMinutes &&
                task.pomodoros != null
            }
            .sortedBy { task ->
                task.startTimeInMinutes
            }

        // todo(move priority list to Task + change priority order)
        // then we show tasks which don't have time start and time end
        val tasksWithoutTime = tasksOfToday
            .filter { task ->
                task.startTimeInMinutes == null &&
                task.pomodoros != null
            }
            .sortedBy { task ->
                task.priority
            }

        val tasks: MutableList<Task> = mutableListOf()
        tasks.addAll(tasksWithTime)
        tasks.addAll(tasksWithoutTime)

        return tasks
    }
}
