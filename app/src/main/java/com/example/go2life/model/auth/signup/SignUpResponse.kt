package com.example.go2life.model.auth.signup

data class SignUpResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val success: Boolean
)