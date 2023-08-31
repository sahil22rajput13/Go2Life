package com.example.go2life.utils

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.example.go2life.base.GetObjects
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class Utility {
    companion object {

        fun isNetworkConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            return cm?.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
        }

        fun getDeviceToken() {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                Log.d("device_token", token)
                GetObjects.preference.putString(SharedPreference.Key.DEVICETOKEN, token)
            })
        }
    }
}