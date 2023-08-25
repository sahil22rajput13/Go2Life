package com.example.go2life.model.comment.postDetail

data class PostResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val success: Boolean
)