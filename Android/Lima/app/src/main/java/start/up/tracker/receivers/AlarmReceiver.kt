package start.up.tracker.receivers;

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import start.up.tracker.entities.Notification
import start.up.tracker.utils.TimeHelper
import start.up.tracker.utils.notifications.sendNotification


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        if (context == null) {
            return
        }

        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            // TODO reschedule notification
        } else {
//            if (!intent.getBooleanExtra(RECURRING, false)) {
//                startAlarmService(context, intent)
//            }
            run {
                if (isAlarmToday(intent)) {
                    sendNotification(context, intent)
                }
            }
        }
    }

    private fun isAlarmToday(intent: Intent): Boolean {
        val currentDateTime = TimeHelper.getCurrentDayInMilliseconds()
        val notificationDateTime = intent.getLongExtra("TRIGGER_TIME", 0)

        return TimeHelper.getDifferenceOfDatesInDays(currentDateTime, notificationDateTime)!! == 0L
    }

    private fun sendNotification(context: Context, intent: Intent) {
        val gson = Gson()
        val strEntity = intent.getStringExtra("NOTIFICATION")
        val entity = gson.fromJson(strEntity, Notification::class.java)
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendNotification(
            entity,
            context
        )
    }
}