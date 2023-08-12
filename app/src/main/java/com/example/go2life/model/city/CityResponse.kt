package com.example.go2life.model.city

data class CityResponse(
    val body: List<Body>,
    val code: Int,
    val message: String,
    val success: Boolean
)