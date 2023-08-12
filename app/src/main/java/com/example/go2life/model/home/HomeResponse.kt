package com.example.go2life.model.home

data class HomeResponse(
    val body: ArrayList<Body>,
    val code: Int,
    val count: Int,
    val message: String,
    val success: Boolean
)