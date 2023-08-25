package com.example.go2life.model.homeData.getReels

data class getReelsResponse(
    val body: List<Body>,
    val code: Int,
    val message: String,
    val success: Boolean
)