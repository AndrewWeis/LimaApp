package start.up.tracker.analytics

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import start.up.tracker.database.dao.AnalyticsDao
import start.up.tracker.database.dao.TaskAnalyticsDao
import start.up.tracker.entities.DayStat
import start.up.tracker.entities.Task
import start.up.tracker.entities.TaskAnalytics
import start.up.tracker.utils.TimeHelper
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Analytics @Inject constructor(
    private val analyticsDao: AnalyticsDao,
) {

    // done
    suspend fun addTaskToStatisticOnCompletion() = withContext(Dispatchers.Default) {
        val calendar = Calendar.getInstance()
        val currentYear: Int = calendar.get(Calendar.YEAR)
        val currentMonth: Int = calendar.get(Calendar.MONTH) + 1
        val currentDay: Int = calendar.get(Calendar.DAY_OF_MONTH)

        var dayStat: DayStat? = analyticsDao.getStatDay(currentYear, currentMonth, currentDay)

        if (dayStat == null) {
            dayStat = DayStat(day = currentDay, month = currentMonth, year = currentYear,
                completedTasks = 1)
            analyticsDao.insertDayStat(dayStat)
        } else {
            val newDayStat = dayStat.copy(completedTasks = dayStat.completedTasks + 1)
            analyticsDao.updateDayStat(newDayStat)
        }
    }

    // done
    suspend fun addTaskToStatisticOnCreate(date: Long?) = withContext(Dispatchers.Default) {
        val calendar = Calendar.getInstance()
        val currentYear: Int
        val currentMonth: Int
        val currentDay: Int

        if (date == null) {
            currentYear = calendar.get(Calendar.YEAR)
            currentMonth = calendar.get(Calendar.MONTH) + 1
            currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        } else {
            currentYear = TimeHelper.getCurrentYearFromMillis(date)
            currentMonth = TimeHelper.getCurrentMonthFromMillis(date) + 1
            currentDay = TimeHelper.getCurrentDayFromMillis(date)
        }

        var dayStat: DayStat? = analyticsDao.getStatDay(currentYear, currentMonth, currentDay)

        if (dayStat == null) {
            dayStat =
                DayStat(day = currentDay, month = currentMonth, year = currentYear, allTasks = 1)
            analyticsDao.insertDayStat(dayStat)
        } else {
            val newDayStat = dayStat.copy(allTasks = dayStat.allTasks + 1)
            analyticsDao.updateDayStat(newDayStat)
        }
    }

    // done
    suspend fun addTaskToStatisticOnEdit(beforeDate: Long?, afterDate: Long?) =
        withContext(Dispatchers.Default) {
            val calendar = Calendar.getInstance()
            var currentYear: Int
            var currentMonth: Int
            var currentDay: Int
            var dayStat: DayStat?

            if (beforeDate == null) {
                currentYear = calendar.get(Calendar.YEAR)
                currentMonth = calendar.get(Calendar.MONTH) + 1
                currentDay = calendar.get(Calendar.DAY_OF_MONTH)
            } else {
                currentYear = TimeHelper.getCurrentYearFromMillis(beforeDate)
                currentMonth = TimeHelper.getCurrentMonthFromMillis(beforeDate) + 1
                currentDay = TimeHelper.getCurrentDayFromMillis(beforeDate)
            }

            dayStat = analyticsDao.getStatDay(currentYear, currentMonth, currentDay)

            if (dayStat == null) {
                dayStat = DayStat(day = currentDay,
                    month = currentMonth,
                    year = currentYear,
                    allTasks = 0)
                analyticsDao.insertDayStat(dayStat)
            } else {
                val newDayStat = dayStat.copy(allTasks = dayStat.allTasks - 1)
                analyticsDao.updateDayStat(newDayStat)
            }

            /////////////////////////

            if (afterDate == null) {
                currentYear = calendar.get(Calendar.YEAR)
                currentMonth = calendar.get(Calendar.MONTH) + 1
                currentDay = calendar.get(Calendar.DAY_OF_MONTH)
            } else {
                currentYear = TimeHelper.getCurrentYearFromMillis(afterDate)
                currentMonth = TimeHelper.getCurrentMonthFromMillis(afterDate) + 1
                currentDay = TimeHelper.getCurrentDayFromMillis(afterDate)
            }

            dayStat = analyticsDao.getStatDay(currentYear, currentMonth, currentDay)

            if (dayStat == null) {
                dayStat = DayStat(day = currentDay,
                    month = currentMonth,
                    year = currentYear,
                    allTasks = 1)
                analyticsDao.insertDayStat(dayStat)
            } else {
                val newDayStat = dayStat.copy(allTasks = dayStat.allTasks + 1)
                analyticsDao.updateDayStat(newDayStat)
            }
        }
}
