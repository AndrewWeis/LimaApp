package start.up.tracker.utils

import android.text.format.DateFormat.is24HourFormat
import start.up.tracker.application.App
import start.up.tracker.entities.Task
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object TimeHelper {

    val isSystem24Hour = is24HourFormat(App.context)

    /**
     * Получить текущую дату в миллисекундах
     *
     * @return милисекунды
     */
    fun getCurrentDayInMilliseconds(): Long {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
        calendar.clear()
        calendar.set(year, month, dayOfMonth)
        return calendar.timeInMillis
    }

    fun getCurrentYearFromMillis(millis: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.clear()
        calendar.timeInMillis = millis
        return calendar[Calendar.YEAR]
    }

    fun getCurrentMonthFromMillis(millis: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.clear()
        calendar.timeInMillis = millis
        return calendar[Calendar.MONTH]
    }

    fun getCurrentWeekFromMillis(millis: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.clear()
        calendar.timeInMillis = millis
        return calendar[Calendar.WEEK_OF_YEAR]
    }

    fun getCurrentDayFromMillis(millis: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.clear()
        calendar.timeInMillis = millis
        return calendar[Calendar.DAY_OF_MONTH]
    }

    /**
     * Получить дату в миллисекундах
     *
     * @param year год
     * @param month месяц
     * @param dayOfMonth день месяца
     * @return милисекунды
     */
    fun getDateInMilliseconds(year: Int, month: Int, dayOfMonth: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.clear()
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month
        calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
        return calendar.timeInMillis
    }

    /**
     * Получить текущую дату и время в милисекундах
     *
     * @return милисекунды
     */
    fun getCurrentTimeInMilliseconds(): Long {
        return Calendar.getInstance().timeInMillis
    }

    /**
     * Получить минуты текущего часа текущего дня
     *
     * @return минуты
     */
    fun getMinutesOfCurrentHour(): Int {
        return Calendar.getInstance().get(Calendar.MINUTE)
    }

    /**
     * Получить часы текущего дня
     *
     * @return часы
     */
    fun getHoursOfCurrentDay(): Int {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    }

    /**
     * Получить минуты текущего дня
     *
     * @return минуты
     */
    fun getMinutesOfCurrentDay(): Int {
        return getHoursOfCurrentDay() * 60 + getMinutesOfCurrentHour()
    }

    /**
     * форматирует минуты текущего дня в вид hh:mm или HH:mm
     *
     * @param minutesOfDay минуты текущего дня
     * @return форматированное время
     */
    fun formatMinutesOfCurrentDay(minutesOfDay: Int?): String? {
        if (minutesOfDay == null) {
            return null
        }

        // если значение от 0 до 9, нужно добавить нолик слева
        var minutes = (minutesOfDay % 60).toString()
        if (minutesOfDay % 60 / 10 == 0) {
            minutes = "0$minutes"
        }

        return if (isSystem24Hour) {
            val hours = minutesOfDay / 60
            "$hours:$minutes"
        } else {
            if (minutesOfDay > 720) {
                val hours = (minutesOfDay - 720) / 60
                "$hours:$minutes pm"
            } else {
                val hours = minutesOfDay / 60
                "$hours:$minutes am"
            }
        }
    }

    /**
     * форматирует дату в миллисекундах в дату вида [DateFormats]
     *
     * @param milliseconds миллисекундах даты
     * @param format вид даты
     * @return форматированная дата
     */
    fun formatMillisecondToDate(milliseconds: Long?, format: String): String? {
        milliseconds?.let {
            val dateFormat = SimpleDateFormat(format)
            val date = Date(it)
            return dateFormat.format(date)
        }

        return null
    }

    /**
     * Получить дату в виде строки вида [DateFormats]
     *
     * @param format вид даты
     * @return форматированная дата
     */
    fun getTodayAsString(format: String): String {
        val currentDayMillis = getCurrentDayInMilliseconds()
        return formatMillisecondToDate(currentDayMillis, format) ?: ""
    }

    /**
     * Найти разницу в днях между двумя датами.
     *
     * @param date1 первая дата
     * @param date2 вторая дата
     * @return разница в днях
     */
    fun getDifferenceOfDatesInDays(date1: Long?, date2: Long?): Long? {
        if (date1 != null && date2 != null) {
            val dif: Long = date1 - date2
            return if (dif > 0) {
                TimeUnit.MILLISECONDS.toDays(dif)
            } else {
                -1 * (TimeUnit.MILLISECONDS.toDays(-1 * dif))
            }
        }
        return null
    }

    /**
     * Метод вычисляет дату окончания активности в миллисекундах
     * Если время окончания в минутах вно не указано, то берётся за основу
     * конец дня
     */
    // todo(убрать лишнее как прийдет время)
    fun computeEndDate(task: Task): Long {
        return if (task.date != null) {
            if (task.endTimeInMinutes != null) {
                task.date + task.endTimeInMinutes.toLong() * 60 * 1000
            } else {
                task.date + 24 * 60 * 60 * 1000 - 1
            }
        } else {
            if (task.endTimeInMinutes != null) {
                getCurrentDayInMilliseconds() + task.endTimeInMinutes.toLong() * 60 * 1000
            } else {
                getCurrentDayInMilliseconds() + 24 * 60 * 60 * 1000 - 1
            }
        }
    }

    /**
     * Метод вычисляет дату начала активности в миллисекундах
     * Если время начала в минутах явно не указано, то берётся за основу
     * начало дня
     */
    // todo(убрать лишнее как прийдет время)
    fun computeStartDate(task: Task): Long {
        return if (task.date != null) {
            if (task.startTimeInMinutes != null) {
                task.date + task.startTimeInMinutes.toLong() * 60 * 1000
            } else {
                task.date
            }
        } else {
            if (task.startTimeInMinutes != null) {
                getCurrentDayInMilliseconds() + task.startTimeInMinutes.toLong() * 60 * 1000
            } else {
                getCurrentDayInMilliseconds()
            }
        }
    }

    object DateFormats {
        const val DD_MMMM: String = "dd-MMMM"
        const val DD_MMM_EEEE: String = "dd MMM, EEEE"
    }
}
