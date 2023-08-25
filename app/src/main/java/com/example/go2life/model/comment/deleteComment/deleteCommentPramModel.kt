package com.example.go2life.model.comment.deleteComment

data class deleteCommentPramModel(
    val comment_id: String,
    val post_id: String,
    val reason: String
)