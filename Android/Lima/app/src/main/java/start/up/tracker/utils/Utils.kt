package start.up.tracker.utils

import android.content.res.Resources
import start.up.tracker.data.models.Task
import start.up.tracker.data.models.TodayTask

val <T> T.exhaustive: T
    get() = this


fun TodayTask.toTask() = Task(
    taskName = taskName,
    priority = priority,
    completed = completed,
    created = created,
    id = id,
    date = date,
    timeStart = timeStart,
    timeEnd = timeEnd,
    timeStartInt = timeStartInt,
    timeEndInt = timeEndInt
)

fun timeToMinutes(strInitial: String): Int {

    val strHourList = strInitial.toList().takeWhile { it != ':' }
    var strHourTime = ""
    strHourList.forEach { strHourTime += it }

    val strMinuteList = strInitial.substringAfter(":").toList()
    var strMinuteTime = ""
    strMinuteList.forEach { strMinuteTime += it }

    return 60 * strHourTime.toInt() + strMinuteTime.toInt()
}

fun convertDpToPx(dp: Int) = (dp * Resources.getSystem().displayMetrics.density).toInt()