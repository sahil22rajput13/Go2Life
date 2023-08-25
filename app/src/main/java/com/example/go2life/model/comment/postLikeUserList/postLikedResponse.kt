package com.example.go2life.model.comment.postLikeUserList

data class postLikedResponse(
    val body: ArrayList<Body>,
    val code: Int,
    val message: String,
    val success: Boolean
)