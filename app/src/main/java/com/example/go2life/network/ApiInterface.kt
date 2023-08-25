package com.example.go2life.network

import com.example.go2life.model.auth.city.CityPramModel
import com.example.go2life.model.auth.city.CityResponse
import com.example.go2life.model.auth.country.CountryResponse
import com.example.go2life.model.auth.login.LoginPramModel
import com.example.go2life.model.auth.login.LoginResponse
import com.example.go2life.model.auth.profile.ProfilePramModel
import com.example.go2life.model.auth.profile.ProfileResponse
import com.example.go2life.model.auth.signup.SignUpPramModel
import com.example.go2life.model.auth.signup.SignUpResponse
import com.example.go2life.model.comment.deleteComment.deleteCommentPramModel
import com.example.go2life.model.comment.deleteComment.deleteCommentResponse
import com.example.go2life.model.comment.postDelete.deleteMyPostPramModel
import com.example.go2life.model.comment.postDelete.deleteMyPostResponse
import com.example.go2life.model.comment.postDetail.PostPramModel
import com.example.go2life.model.comment.postDetail.PostResponse
import com.example.go2life.model.comment.postLikeUserList.postLikedPramModel
import com.example.go2life.model.comment.postLikeUserList.postLikedResponse
import com.example.go2life.model.comment.postlikeComment.postLikePramModel
import com.example.go2life.model.comment.postlikeComment.postLikeResponse
import com.example.go2life.model.comment.postunlike.PostUnlikePramModel
import com.example.go2life.model.comment.postunlike.PostUnlikeResponse
import com.example.go2life.model.homeData.getNotification.getNotificationResponse
import com.example.go2life.model.homeData.getNotification.notificationRead.notificationReadResponse
import com.example.go2life.model.homeData.home.HomePramModel
import com.example.go2life.model.homeData.home.HomeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {
    @POST("/users/v2/register")
    suspend fun onSignUp(@Body body: SignUpPramModel): Response<SignUpResponse>

    @GET("/users/getCountry")
    suspend fun onCountry(): Response<CountryResponse>

    @POST("/users/getCity")
    suspend fun onCity(@Body body: CityPramModel): Response<CityResponse>

    @POST("/users/v2/complete-profile")
    suspend fun onProfile(@Body body: ProfilePramModel): Response<ProfileResponse>

    @POST("/users/login")
    suspend fun onLogin(@Body body: LoginPramModel): Response<LoginResponse>

    @POST("users/postList")
    suspend fun onHome(@Body body: HomePramModel): Response<HomeResponse>

    @POST("users/postDetail")
    suspend fun onPost(@Body body: PostPramModel): Response<PostResponse>

    @POST("users/postlikeComment")
    suspend fun onPostLike(@Body body: postLikePramModel): Response<postLikeResponse>

    @POST("users/postUnlike")
    suspend fun onPostLike(@Body body: PostUnlikePramModel): Response<PostUnlikeResponse>

    @POST("users/postLikeUserList")
    suspend fun onPostLiked(@Body body: postLikedPramModel): Response<postLikedResponse>

    @POST("users/deleteMyPost")
    suspend fun onDeleteMyPost(@Body body: deleteMyPostPramModel): Response<deleteMyPostResponse>

    @POST("users/deleteComment")
    suspend fun onDeleteComment(@Body body: deleteCommentPramModel): Response<deleteCommentResponse>

    @GET("users/getNotification")
    suspend fun onGetNotification(): Response<getNotificationResponse>

    @GET("users/notificationRead")
    suspend fun onNotificationRead(@Query("notification_id") notification_id: String?): Response<notificationReadResponse>

}