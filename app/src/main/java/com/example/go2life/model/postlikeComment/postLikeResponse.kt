package com.example.go2life.model.postlikeComment

data class postLikeResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val success: Boolean
)