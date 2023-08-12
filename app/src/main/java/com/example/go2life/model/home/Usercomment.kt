package com.example.go2life.model.home

data class Usercomment(
    val categoryname: String,
    val commentby: String,
    val commentreply: List<Any>,
    val created_at: String,
    val id: Int,
    val message: String,
    val post_id: Int,
    val profilepic: String,
    val updated_at: String,
    val user_id: Int
)