package com.example.go2life.model.postDetail

import com.example.go2life.model.home.Jobdata
import com.example.go2life.model.home.Usercomment

data class Body(
    val commentcount: Int,
    val company_name: String,
    val course_id: Int,
    val coursedata: Any,
    val created_at: String,
    val description: String,
    val hashtags: String,
    val id: Int,
    val is_liked: Int,
    val job_id: Any,
    val jobdata: Jobdata,
    val latitude: Any,
    val likecount: Int,
    val location: String,
    val longitude: Any,
    val mentionuser: Any,
    val mentionuserdata: List<Any>,
    val role: String,
    val seekercategoryname: String,
    val updated_at: Any,
    val user_id: Int,
    val usercomment: ArrayList<Usercomment>,
    val userdata: Userdata,
    val userpostgallerydata: List<Userpostgallerydata>
)