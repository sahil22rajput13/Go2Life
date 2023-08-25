package com.example.go2life.model.comment.postDelete

data class deleteMyPostResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val success: Boolean
)