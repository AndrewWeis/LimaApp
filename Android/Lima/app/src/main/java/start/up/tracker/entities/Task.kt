package start.up.tracker.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val taskId: Int = 0,
    val taskTitle: String = "",
    val description: String = "",
    val priority: Int = 0,
    val completed: Boolean = false,
    val created: Long = System.currentTimeMillis(),
    val startTimeInMinutes: Int? = null,
    val endTimeInMinutes: Int? = null,
    val date: Long? = null,

    // для определения к какому таску относится текущая
    val parentTaskId: Int = -1,
    val subtasksNumber: Int = 0,
    val completedSubtasksNumber: Int = 0,

    // todo (maybe add task id to day stat)
    val wasCompleted: Boolean = false,

    val categoryId: Int = 1, // 1 - inbox
    val categoryName: String? = null,
    val categoryColor: Int? = null,
) : Parcelable
