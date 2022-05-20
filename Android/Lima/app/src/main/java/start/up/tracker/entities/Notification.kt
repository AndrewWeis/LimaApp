package start.up.tracker.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import start.up.tracker.utils.TimeHelper

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
                NotificationType.AT_TASK_TIME -> "Время выполнения задачи истекло."
                NotificationType.FIVE_MINUTES_BEFORE -> "Задача завершится через 5 минут, поторопитесь"
                NotificationType.TEN_MINUTES_BEFORE -> "Задача завершится через 10 минут, поторопитесь"
                NotificationType.FIFTEEN_MINUTES_BEFORE -> "Задача завершится через 15 минут, поторопитесь"
                NotificationType.THIRTY_MINUTES_BEFORE -> "Задача завершится через 30 минут, поторопитесь"
                NotificationType.ONE_HOUR_BEFORE -> "Задача завершится через 1 час, поторопитесь"
                NotificationType.TWO_HOURS_BEFORE -> "Задача завершится через 2 часа, поторопитесь"
                NotificationType.ONE_DAY_BEFORE -> "Задача завершится через 1 день, поторопитесь"
            }
        }
    }
}