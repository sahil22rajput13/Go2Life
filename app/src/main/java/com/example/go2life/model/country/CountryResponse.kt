package com.example.go2life.model.country

data class CountryResponse(
    val body: List<Body>,
    val code: Int,
    val message: String,
    val success: Boolean
)