package start.up.tracker.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import start.up.tracker.R
import start.up.tracker.utils.TimeHelper
import start.up.tracker.utils.resources.ResourcesUtils

@Parcelize
@Entity(tableName = "notification_table")
data class Notification(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var isActive: Boolean = false,
    val type: NotificationType = NotificationType.NONE,
    val triggerDateTimeInMillis: Long? = null,
    val title: String = "",
    val body: String = ""
) : Parcelable {

    fun copyFromType(type: NotificationType, taskEnd: Long):Notification {
        val triggerTime = getNotificationTriggerTime(type, taskEnd)
        val body = getBodyFromType(type)

        return this.copy(type = type, triggerDateTimeInMillis = triggerTime, body = body)
    }

    companion object {
        fun create(type: NotificationType, taskEnd: Long): Notification {
            val triggerTime = getNotificationTriggerTime(type, taskEnd)

            val title = "Task notification"
            val body = getBodyFromType(type)

            return Notification(type = type, triggerDateTimeInMillis = triggerTime, title = title, body = body)
        }

        fun getNotificationTriggerTime(type: NotificationType, taskEnd: Long): Long? {
            return when (type) {
                NotificationType.NONE -> null
                NotificationType.AT_TASK_TIME -> taskEnd
                NotificationType.FIVE_MINUTES_BEFORE -> TimeHelper.addMinutes(taskEnd, -5)
                NotificationType.TEN_MINUTES_BEFORE -> TimeHelper.addMinutes(taskEnd, -10)
                NotificationType.FIFTEEN_MINUTES_BEFORE -> TimeHelper.addMinutes(taskEnd, -15)
                NotificationType.THIRTY_MINUTES_BEFORE -> TimeHelper.addMinutes(taskEnd, -30)
                NotificationType.ONE_HOUR_BEFORE -> TimeHelper.addMinutes(taskEnd, -60)
                NotificationType.TWO_HOURS_BEFORE -> TimeHelper.addMinutes(taskEnd, -120)
                NotificationType.ONE_DAY_BEFORE -> TimeHelper.addDays(taskEnd, -1)
            }
        }

        private fun getBodyFromType(type: NotificationType): String {
            return when (type) {
                NotificationType.NONE -> ""
                NotificationType.AT_TASK_TIME -> ResourcesUtils.getString(R.string.notification_at_task_time)
                NotificationType.FIVE_MINUTES_BEFORE -> ResourcesUtils.getString(R.string.notification_5_minutes)
                NotificationType.TEN_MINUTES_BEFORE -> ResourcesUtils.getString(R.string.notification_10_minutes)
                NotificationType.FIFTEEN_MINUTES_BEFORE -> ResourcesUtils.getString(R.string.notification_15_minutes)
                NotificationType.THIRTY_MINUTES_BEFORE -> ResourcesUtils.getString(R.string.notification_30_minutes)
                NotificationType.ONE_HOUR_BEFORE -> ResourcesUtils.getString(R.string.notification_1_hour)
                NotificationType.TWO_HOURS_BEFORE -> ResourcesUtils.getString(R.string.notification_2_hours)
                NotificationType.ONE_DAY_BEFORE -> ResourcesUtils.getString(R.string.notification_1_day)
            }
        }
    }
}
