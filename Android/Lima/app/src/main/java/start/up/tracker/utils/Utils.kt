package start.up.tracker.utils

import android.content.res.Resources
import start.up.tracker.R
import start.up.tracker.data.models.Task
import start.up.tracker.data.models.ExtendedTask

val <T> T.exhaustive: T
    get() = this


fun ExtendedTask.toTask() = Task(
    taskName = taskName,
    taskDesc = taskDesc,
    priority = priority,
    completed = completed,
    created = created,
    taskId = taskId,
    date = date,
    dateLong = dateLong,
    timeStart = timeStart,
    timeEnd = timeEnd,
    timeStartInt = timeStartInt,
    timeEndInt = timeEndInt,
    wasCompleted = wasCompleted
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

fun chooseIconDrawable(priority: Int): Int {
    return when(priority) {
        1 -> R.drawable.ic_priority_1
        2 -> R.drawable.ic_priority_2
        3 -> R.drawable.ic_priority_3
        else -> R.drawable.ic_android // This should never be reached
    }
}