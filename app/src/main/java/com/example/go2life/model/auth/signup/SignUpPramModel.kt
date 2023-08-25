package com.example.go2life.model.auth.signup

data class SignUpPramModel(
    val deviceToken: String,
    val deviceType: String,
    val email: String,
    val first_name: String,
    val password: String,
    val phone_no: String,
    val username: String
)