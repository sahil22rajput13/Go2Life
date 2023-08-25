package com.example.go2life.model.comment.postlikeComment

data class postLikeResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val success: Boolean
)