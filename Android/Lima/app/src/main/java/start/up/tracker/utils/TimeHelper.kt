package start.up.tracker.utils

import android.text.format.DateFormat.is24HourFormat
import com.google.android.material.datepicker.MaterialDatePicker
import start.up.tracker.application.App
import java.text.SimpleDateFormat
import java.util.*

object TimeHelper {

    val isSystem24Hour = is24HourFormat(App.context)

    /**
     * Получить текущую дату
     *
     * @return милисекунды
     */
    fun getCurrentDayInMillisecond(): Long {
        return MaterialDatePicker.todayInUtcMilliseconds()
    }

    /**
     * Получить текущую дату и время в милисекундах
     *
     * @return милисекунды
     */
    fun getCurrentTimeInMillisecond(): Long {
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
        const val DD_MM: String = "dd-MM"
    }
}
