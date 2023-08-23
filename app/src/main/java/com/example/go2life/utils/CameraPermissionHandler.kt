package com.example.go2life.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

class CameraPermissionHandler {

    fun openAppSettings(activity: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        activity.startActivity(intent)
    }
}