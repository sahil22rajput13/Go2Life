package com.example.go2life.model.homeData.getNotification

data class getNotificationResponse(
    val body: ArrayList<Body>,
    val code: Int,
    val message: String,
    val success: Boolean
)