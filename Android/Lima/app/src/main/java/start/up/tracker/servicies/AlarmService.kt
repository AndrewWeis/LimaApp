package start.up.tracker.servicies

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import start.up.tracker.application.App
import start.up.tracker.entities.Notification
import start.up.tracker.receivers.AlarmReceiver
import start.up.tracker.utils.TimeHelper
import java.util.*

fun schedule(notification: Notification) {
        if (notification.triggerDateTimeInMillis == null) {
                return
        }

        val context = App.context
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        val gson = Gson()

        intent.putExtra("NOTIFICATION", gson.toJson(notification))
        intent.putExtra("TRIGGER_TIME", notification.triggerDateTimeInMillis)
        intent.putExtra("TITLE", "some notification title");

        val alarmPendingIntent = PendingIntent.getBroadcast(context, notification.id.toInt(), intent, 0)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = notification.triggerDateTimeInMillis

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmPendingIntent
        )

        notification.isActive = true;
}