package com.example.go2life.model.auth.login

data class Body(
    val token: String,
    val userDetails: UserDetails
)