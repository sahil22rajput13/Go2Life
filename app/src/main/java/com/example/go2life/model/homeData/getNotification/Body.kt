package com.example.go2life.model.homeData.getNotification

data class Body(
    val created_at: String,
    val id: Int,
    val is_read: Int,
    val job_id: String,
    val message: String,
    val post_id: String,
    val reciever_id: Int,
    val reciever_name: String,
    val sender_id: Int,
    val type: Int,
    val updated_at: String,
    val usersenderdata: Usersenderdata
)