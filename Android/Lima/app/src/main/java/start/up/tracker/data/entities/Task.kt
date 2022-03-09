package start.up.tracker.data.entities

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
    val taskId: Int = 0,
    val taskName: String,
    val taskDesc: String,
    val priority: Int = 1,
    val completed: Boolean = false,
    val created: Long = System.currentTimeMillis(),
    val date: String? = null,
    val dateLong: Long?,
    val timeStart: String?,
    val timeEnd: String?,
    val timeStartInt: Int?,
    val timeEndInt: Int?,
    val categoryId: Int = 0, // 0 - inbox

    // todo (maybe add task id to day stat)
    val wasCompleted: Boolean = false,

    val categoryName: String? = null,
    val color: Int? = null,
) : Parcelable
