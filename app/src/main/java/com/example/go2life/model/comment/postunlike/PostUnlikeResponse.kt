package com.example.go2life.model.comment.postunlike

data class PostUnlikeResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val success: Boolean
)