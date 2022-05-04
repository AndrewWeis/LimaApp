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
    val type: NotificationType = NotificationType.NONE,
    val triggerTime: Long = 0,
) : Parcelable