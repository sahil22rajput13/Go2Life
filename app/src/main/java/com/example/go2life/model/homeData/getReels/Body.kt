package com.example.go2life.model.homeData.getReels

data class Body(
    val commentcount: Int,
    val created_at: String,
    val description: String,
    val hashtags: String,
    val id: Int,
    val is_liked: Int,
    val latitude: String,
    val likecount: Int,
    val longitude: String,
    val reelviewcount: Int,
    val updated_at: String,
    val user_id: Int,
    val userdata: Userdata,
    val video_thum: String,
    val videourl: String,
    val viewcount: Int
)