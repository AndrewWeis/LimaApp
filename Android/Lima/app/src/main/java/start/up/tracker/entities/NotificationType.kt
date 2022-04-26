package start.up.tracker.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import start.up.tracker.R

@Parcelize
enum class NotificationType(val typeId: Int, val resourceId: Int) : Parcelable {
    NONE(0, R.string.notification_none),
    AT_TASK_TIME(1, R.string.notification_task_time),
    FIVE_MINUTES_BEFORE(2, R.string.notification_5_before),
    TEN_MINUTES_BEFORE(3, R.string.notification_10_before),
    FIFTEEN_MINUTES_BEFORE(4, R.string.notification_15_before),
    THIRTY_MINUTES_BEFORE(5, R.string.notification_30_before),
    ONE_HOUR_BEFORE(6, R.string.notification_1_hour_before),
    TWO_HOURS_BEFORE(7, R.string.notification_2_hour_before),
    ONE_DAY_BEFORE(8, R.string.notification_1_day_before);

    companion object {
        fun getByTypeId(typeId: Int) = values().find { it.typeId == typeId }
    }
}