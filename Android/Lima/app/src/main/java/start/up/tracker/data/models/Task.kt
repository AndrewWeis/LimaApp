package start.up.tracker.data.models

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
 *
 * In order to pass task from [TasksFragment] to [AddEditTaskFragment] we need our task implement [Parcelable] interface
 */
@Parcelize
@Entity(tableName = "task_table")
data class Task (
    val taskName: String,
    val important: Boolean = false,
    val completed: Boolean = false,
    val created: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true)  val id: Int = 0,
    val date: String = "No date"
): Parcelable