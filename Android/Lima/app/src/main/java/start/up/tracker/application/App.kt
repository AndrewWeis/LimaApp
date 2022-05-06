package start.up.tracker.application

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import start.up.tracker.R

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        createChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_channel_name)
        )
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.notification_channel_description)

            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(notificationChannel)
        }
    }
}
