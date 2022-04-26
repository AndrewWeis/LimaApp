package start.up.tracker.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "notification_table")
data class Notification(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: Int = NotificationIds.NO_NOTIFICATION,
    val triggerTime: Long = 0,
) : Parcelable