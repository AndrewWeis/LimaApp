package start.up.tracker.utils

import start.up.tracker.data.models.Task
import start.up.tracker.data.models.TodayTask

val <T> T.exhaustive: T
    get() = this


fun TodayTask.toTask() = Task(
    taskName = taskName,
    important = important,
    completed = completed,
    created = created,
    id = id,
    date = date
)