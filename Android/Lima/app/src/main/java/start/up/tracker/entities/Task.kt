package start.up.tracker.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * We made properties immutable by using val because later when we need to update the task
 * instead of changing the existing task object we will create a completely new one and use
 * this one to update our database. We need it to easily find bugs if they occur.
 * Our app may not recognise the changes if we modify exciting item.
 * We crate new items rather than changing existing items
 */
@Parcelize
@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val priority: Int = 0,
    val completed: Boolean = false,
    val created: Long = System.currentTimeMillis(),
    val startTimeInMinutes: Int? = null,
    val endTimeInMinutes: Int? = null,
    val date: Long? = null,

    // todo (maybe add task id to day stat)
    val wasCompleted: Boolean = false,

    val categoryId: Int = 1, // 1 - inbox
    val categoryName: String? = null,
    val color: Int? = null,
) : Parcelable
