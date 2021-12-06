package start.up.tracker.utils

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

    val str = strInitial.toList()

    val strHourList = str.takeWhile { it != ':' }
    var strHourTime = ""
    strHourList.forEach { strHourTime += it }

    val strMinuteList = str.takeLast(2)
    var strMinuteTime = ""
    strMinuteList.forEach { strMinuteTime += it }

    return 60 * strHourTime.toInt() + strMinuteTime.toInt()
}