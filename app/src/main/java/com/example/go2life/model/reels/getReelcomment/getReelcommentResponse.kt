package com.example.go2life.model.reels.getReelcomment

data class getReelcommentResponse(
    val body: ArrayList<Body>,
    val code: Int,
    val message: String,
    val success: Boolean
)