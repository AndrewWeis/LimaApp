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
    val priority: Int = 1,
    val completed: Boolean = false,
    val created: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true)  val id: Int = 0,
    val date: String = "No date",
    val timeStart: String = "No time",
    val timeEnd: String = "No time"
): Parcelable {
    val timeStartInt = convertTimeToInt(timeStart)
    val timeEndInt = convertTimeToInt(timeEnd)
}

fun convertTimeToInt(time: String): Int {
    return when(time) {
        "5:00" -> 1
        "5:30" -> 2
        "6:00" -> 3
        "6:30" -> 4
        "7:00" -> 5
        "7:30" -> 6
        "8:00" -> 7
        "8:30" -> 7
        "9:00" -> 8
        "9:30" -> 9
        "10:00" -> 10
        "10:30" -> 11
        "11:00" -> 12
        "11:30" -> 13
        "12:00" -> 14
        "12:30" -> 15
        "13:00" -> 16
        "13:30" -> 17
        "14:00" -> 18
        "14:30" -> 19
        "15:00" -> 20
        "15:30" -> 21
        "16:00" -> 22
        "16:30" -> 23
        "17:00" -> 24
        "17:30" -> 25
        "18:00" -> 26
        "18:30" -> 27
        "19:00" -> 28
        "19:30" -> 29
        "20:00" -> 30
        "20:30" -> 31
        "21:00" -> 32
        "21:30" -> 33
        "22:00" -> 34
        "22:30" -> 35
        "23:00" -> 36
        "23:30" -> 37
        "00:00" -> 38
        "00:30" -> 39
        "01:00" -> 40
        "01:30" -> 41
        "02:00" -> 42
        "02:30" -> 43
        "03:00" -> 44
        "03:30" -> 45
        "04:00" -> 46
        "04:30" -> 47
        else -> 0
    }
}
