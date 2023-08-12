package com.example.go2life.model.login

data class LoginPramModel(
    val deviceToken: String,
    val deviceType: String,
    val email: String,
    val password: String
)