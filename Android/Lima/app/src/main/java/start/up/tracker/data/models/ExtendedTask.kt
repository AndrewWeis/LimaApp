package start.up.tracker.data.models

import androidx.room.PrimaryKey

data class ExtendedTask(
    @PrimaryKey
    val id: Int,
    val taskName: String,
    val priority: Int,
    val completed: Boolean,
    val created: Long,
    val date: String,
    val dateLong: Long,
    val timeStart: String,
    val timeEnd: String,
    val timeStartInt: Int,
    val timeEndInt: Int,
    val categoryName: String,
    val color: Int,
    val tasksInside: Int
)