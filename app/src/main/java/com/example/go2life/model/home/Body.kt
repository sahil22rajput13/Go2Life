package com.example.go2life.model.home

data class Body(
    val commentcount: Int,
    val company_name: String,
    val course_id: Int,
    val created_at: String,
    val description: String,
    val hashtags: String,
    val id: Int,
    val is_bookmarked: Int,
    val is_liked: Int,
    val job_id: Int,
    val jobdata: Jobdata,
    val latitude: Any,
    val likecount: Int,
    val location: String,
    val longitude: Any,
    val mentionuser: Any,
    val mentionuserdata: List<Mentionuserdata>,
    val role: String,
    val seekercategoryname: String,
    val updated_at: String,
    val user_id: Int,
    val usercomment: List<Usercomment>,
    val userdata: Userdata,
    val userpostgallerydata: List<Userpostgallerydata>
)