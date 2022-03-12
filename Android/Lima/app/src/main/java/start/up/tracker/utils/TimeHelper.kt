package start.up.tracker.utils

import android.text.format.DateFormat.is24HourFormat
import com.google.android.material.datepicker.MaterialDatePicker
import start.up.tracker.application.App
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
    fun getCurrentHourInMinutes(): Int {
        return Calendar.getInstance().get(Calendar.MINUTE)
    }

    /**
     * Получить часы текущего дня
     *
     * @return часы
     */
    fun getCurrentDayInHours(): Int {
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
    fun getCurrentDayInMinutes(): Int {
        return getCurrentDayInHours() * 60 + getCurrentHourInMinutes()
    }
}
