package com.example.go2life.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.go2life.R
import com.example.go2life.base.MyApplication
import com.example.go2life.view.navigation.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.net.URL


class MyFirebaseMessageService : FirebaseMessagingService() {

    val TAG = "FCM Service"
    val count = 0
    private var preferenceHelper: SharedPreference =
        SharedPreference.getInstance(MyApplication.getContext())

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("newToken", token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        try {
            Log.d("remoteMessage", "onMessageReceived: ${remoteMessage.notification?.imageUrl}")
            setNotification(remoteMessage.notification?.title,remoteMessage.notification?.imageUrl.toString())
        }catch (e:Exception){
            e.printStackTrace()
        }

    }


    private fun setNotification(message: String?, imageUrl:String) {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        val result = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH && powerManager.isInteractive || Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH && powerManager.isScreenOn
        val icon = BitmapFactory.decodeStream(URL(imageUrl).openConnection().getInputStream())
        var intent: Intent? = null
        intent = Intent(this, MainActivity::class.java)
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addNextIntentWithParentStack(intent)
        val resultPendingIntent2 =
            stackBuilder.getPendingIntent(12, PendingIntent.FLAG_UPDATE_CURRENT)
        val channelId = "Default"
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
            .setLargeIcon(icon)
            .setSmallIcon(R.drawable.go2life)
            .setContentTitle("Go2Life")
            .setContentText(message).setAutoCancel(true)
            .setFullScreenIntent(resultPendingIntent2, true)
            .setContentIntent(resultPendingIntent2)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            assert(manager != null)
            manager.createNotificationChannel(channel)
        }
        assert(manager != null)
        manager.notify(0, builder.build())
    }

}
