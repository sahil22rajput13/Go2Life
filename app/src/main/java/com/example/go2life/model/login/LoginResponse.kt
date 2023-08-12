package com.example.go2life.model.login

data class LoginResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val success: Boolean
)