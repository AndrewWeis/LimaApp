package start.up.tracker.utils.notifications
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import start.up.tracker.R
import start.up.tracker.entities.Notification
import start.up.tracker.ui.activities.MainActivity
import start.up.tracker.utils.resources.ResourcesUtils


fun buildSystemNotification(source: Notification, context: Context): android.app.Notification {
    val contentIntent = Intent(context, MainActivity::class.java)
    // TODO: Step 1.12 create PendingIntent
    val contentPendingIntent = PendingIntent.getActivity(
        context,
        source.id.toInt(),
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    /*// TODO: Step 2.0 add style
    val eggImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.cooked_egg
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(eggImage)
        .bigLargeIcon(null)*/

    // TODO: Step 1.2 get an instance of NotificationCompat.Builder
    // Build the notification
    val builder = NotificationCompat.Builder(
        context,
        ResourcesUtils.getString(R.string.notification_channel_id)
    )

        // TODO: Step 1.8 use the new 'breakfast' notification channel

        // TODO: Step 1.3 set title, text and icon to builder
        .setSmallIcon(R.drawable.ic_add)
        .setContentTitle(source.title)
        .setContentText(source.body)

        // TODO: Step 1.13 set content intent
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)

        /* // TODO: Step 2.1 add style to builder
         .setStyle(bigPicStyle)
         .setLargeIcon(eggImage)*/


        // TODO: Step 2.5 set priority
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    return builder.build()
}

fun NotificationManager.sendNotification(notification: Notification, context: Context) {
    val systemNotification = buildSystemNotification(notification, context)
    // TODO: Step 1.4 call notify
    notify(notification.id.toInt(), systemNotification)
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}