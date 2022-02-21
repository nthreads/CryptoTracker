package com.nthreads.cryptotracker.domain.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.nthreads.cryptotracker.R

object MyNotificationManager {
    private const val NOTIFICATION_CHANNEL_ID = "CryptoTracker.channel"

    /**
     * Sets up the notification channels for API 26+, since android Oreo notification channel is needed.
     *
     * @param context application context
     * @param name Name of the channel e.g incidents or chat messages
     * @param description description of the channel
     */
    fun createNotificationChannel(
        context: Context,
        name: String = "Crypto Tracker",
        description: String = "Crypto Exchange Rates",
        isMuted: Boolean = false
    ) {
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val importance =
                if (isMuted) NotificationManager.IMPORTANCE_LOW else NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)

            //channel.setShowBadge(!isMuted)
            channel.description = description

            if (isMuted) {
                channel.setSound(null, null)
                channel.enableLights(true)
                channel.lightColor = Color.BLUE
                channel.enableVibration(false)
            }

            NotificationManagerCompat.from(context).createNotificationChannel(channel)
        }
    }

    fun sendNotification(message: String, activity: Context) {
        val notificationManager =
            activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(activity)

        val notification = NotificationCompat.Builder(activity, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle("Alert")
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .build()

        val id = 99 //getUniqueId()
        notificationManager.notify(id, notification)
    }

    private fun getUniqueId() = ((System.currentTimeMillis() % 10000).toInt())
}