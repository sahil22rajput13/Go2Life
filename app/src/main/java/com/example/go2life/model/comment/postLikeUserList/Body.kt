package com.example.go2life.model.comment.postLikeUserList

data class Body(
    val created_at: String,
    val id: Int,
    val likedby: String,
    val post_id: Int,
    val profilepic: String,
    val updated_at: Any,
    val user_id: Int
)