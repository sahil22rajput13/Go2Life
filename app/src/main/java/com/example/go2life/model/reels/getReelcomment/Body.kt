package com.example.go2life.model.reels.getReelcomment

data class Body(
    val comment: String,
    val created_at: String,
    val firstname: String,
    val id: Int,
    val lastname: String,
    val profile_pic: String,
    val reel_id: Int,
    val updated_at: Any,
    val user_id: Int
)