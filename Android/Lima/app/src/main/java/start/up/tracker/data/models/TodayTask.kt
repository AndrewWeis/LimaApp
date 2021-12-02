package start.up.tracker.data.models

import androidx.room.PrimaryKey

data class TodayTask(
    @PrimaryKey
    val id: Int,
    val taskName: String,
    val important: Boolean,
    val completed: Boolean,
    val created: Long,
    val date: String,
    val categoryName: String,
    val color: Int,
    val tasksInside: Int
)