package start.up.tracker.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "notification_table")
data class Notification(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var isActive: Boolean = false,
    val type: NotificationType = NotificationType.NONE,
    var triggerDateTimeInMillis: Long = 0,
) : Parcelable