package start.up.tracker.utils

import android.text.format.DateFormat.is24HourFormat
import start.up.tracker.application.App
import java.text.SimpleDateFormat
import java.util.*

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
        return if (isSystem24Hour) {
            Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        } else {
            Calendar.getInstance().get(Calendar.HOUR)
        }
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
        minutesOfDay?.let {
            val hours = it / 60
            val minutes = it - hours * 60
            return "$hours:$minutes"
        }

        return null
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

    object DateFormats {
        const val DD_MMMM: String = "dd-MMMM"
    }
}
