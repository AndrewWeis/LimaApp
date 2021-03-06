package start.up.tracker.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = false)
    val taskId: Int = 0,
    val taskTitle: String = "",
    val description: String = "",
    val priority: Int = NONE,
    val completed: Boolean = false,
    val created: Long = System.currentTimeMillis(),
    val startTimeInMinutes: Int? = null,
    val endTimeInMinutes: Int? = null,
    val date: Long? = null,

    // для определения к какому таску относится текущая
    val parentTaskId: Int = -1,
    val subtasksNumber: Int = 0,
    val completedSubtasksNumber: Int = 0,

    // для привычек
    val originalTaskId: Int = -1,
    val shift: Long = -1L,

    // todo (maybe add task id to day stat)
    val wasCompleted: Boolean = false,

    val projectId: Int = -1,
    val projectName: String? = null,
    val projectColor: Int? = null,

    // notification link
    val notificationId: Long = -1,

    val repeatsId: Int = NEVER,

    val pomodoros: Int? = null,
    val completedPomodoros: Int? = null,

    val eisenhowerMatrix: Int = NONE,
) : Parcelable {

    // todo (вынестим в отдельный класс)
    companion object Ids {
        const val NONE = 0

        const val PRIORITY_LOW = 1
        const val PRIORITY_MEDIUM = 2
        const val PRIORITY_HIGH = 3

        const val NEVER = 0
        const val EVERY_DAY = 1
        const val EVERY_WEEK = 2
        const val EVERY_SECOND_WEEK = 3
        const val EVERY_YEAR = 4

        const val IMPORTANT_URGENT = 1
        const val IMPORTANT_NOT_URGENT = 2
        const val NOT_IMPORTANT_URGENT = 3
        const val NOT_IMPORTANT_NOT_URGENT = 4
    }
}
