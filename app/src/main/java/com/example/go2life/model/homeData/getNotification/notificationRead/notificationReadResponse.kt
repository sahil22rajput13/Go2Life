package com.example.go2life.model.homeData.getNotification.notificationRead

data class notificationReadResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val success: Boolean
)