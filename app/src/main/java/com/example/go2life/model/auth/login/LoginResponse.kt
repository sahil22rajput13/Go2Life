package com.example.go2life.model.auth.login

data class LoginResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val success: Boolean
)