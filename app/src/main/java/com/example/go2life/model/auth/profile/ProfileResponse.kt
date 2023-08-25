package com.example.go2life.model.auth.profile

data class ProfileResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val success: Boolean
)