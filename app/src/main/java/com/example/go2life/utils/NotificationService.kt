package com.example.go2life.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.go2life.R
import com.example.go2life.view.navigation.MainActivity

class NotificationService(private val context: Context) {

    private val notificationHelper = NotificationHelper(context)

    private val channelId = "notification_channel"
    private val channelName = "Notification Channel"
    private val channelDescription = "Sample notification channel"

    init {
        notificationHelper.createNotificationChannel(channelId, channelName, channelDescription)
    }

    @SuppressLint("MissingPermission")
    fun showNotification(title: String, message: String) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.go2life) // Make sure you have 'go2life.png' in your drawable folder
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        if (!notificationManager.areNotificationsEnabled()) {
            // Notifications are disabled, show pop-up dialog
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setTitle("Notifications Disabled")
            alertDialogBuilder.setMessage("Notifications are disabled for this app. Do you want to enable them in settings?")
            alertDialogBuilder.setPositiveButton("Settings") { _, _ ->
                val settingsIntent = Intent()
                settingsIntent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                settingsIntent.putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
                context.startActivity(settingsIntent)
            }
            alertDialogBuilder.setNegativeButton("Cancel") { _, _ ->
                // User chose to cancel, return to the app
                // You can add any desired action here
            }
            alertDialogBuilder.setCancelable(false)
            alertDialogBuilder.show()
        } else {
            notificationManager.notify(1, notification)
        }
    }
}
