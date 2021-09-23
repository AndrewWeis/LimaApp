package start.up.tracker.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "task_table")
data class Task (

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val label: String,
    val desc: String,
    val priority: String,
    val category: String,
    val dateAndTime: String,
    val repeats: String

): Parcelable