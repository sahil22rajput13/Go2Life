package com.example.go2life.model.auth.signup

data class Body(
    val token: String,
    val userDetails: UserDetails
)