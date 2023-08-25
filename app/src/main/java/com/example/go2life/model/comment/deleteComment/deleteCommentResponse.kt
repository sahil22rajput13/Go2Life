package com.example.go2life.model.comment.deleteComment

data class deleteCommentResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val success: Boolean
)