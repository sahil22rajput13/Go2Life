package com.example.go2life.model.postLiked

data class postLikedResponse(
    val body: List<Body>,
    val code: Int,
    val message: String,
    val success: Boolean
)