package start.up.tracker.data.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "task_table")
data class Task (
    val name: String,
    val important: Boolean = false,
    val completed: Boolean = false,
    @PrimaryKey(autoGenerate = true)  val id: Int = 0,
    /*val desc: String,
    val priority: String,
    val category: String,
    val dateAndTime: String,
    val repeats: String*/
): Parcelable