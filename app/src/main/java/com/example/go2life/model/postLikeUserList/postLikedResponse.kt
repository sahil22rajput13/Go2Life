package com.example.go2life.model.postLikeUserList

data class postLikedResponse(
    val body: ArrayList<Body>,
    val code: Int,
    val message: String,
    val success: Boolean
)