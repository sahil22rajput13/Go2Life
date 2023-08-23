package com.example.go2life.model.post

data class PostResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val success: Boolean
)