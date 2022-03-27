package start.up.tracker.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "technique_table")
@Parcelize
data class Technique(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String,
    val body: String,
    val timeToRead: Int,
    val isEnabled: Boolean = false,
    val isNotificationsEnabled: Boolean = false,
) : Parcelable
