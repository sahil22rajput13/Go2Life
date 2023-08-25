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
import retrofit2.Response

class Repository {
    private val getApi = GetApi.api

    suspend fun onSignUp(body: SignUpPramModel): Response<SignUpResponse> {
        return getApi.onSignUp(body)
    }

    suspend fun onCountry(): Response<CountryResponse> {
        return getApi.onCountry()
    }

    suspend fun onCity(body: CityPramModel): Response<CityResponse> {
        return getApi.onCity(body)
    }

    suspend fun onProfile(body: ProfilePramModel): Response<ProfileResponse> {
        return getApi.onProfile(body)
    }

    suspend fun onLogin(body: LoginPramModel): Response<LoginResponse> {
        return getApi.onLogin(body)
    }

    suspend fun onPost(body: PostPramModel): Response<PostResponse> {
        return getApi.onPost(body)
    }

    suspend fun onPostLike(body: postLikePramModel): Response<postLikeResponse> {
        return getApi.onPostLike(body)
    }

    suspend fun onPostUnLike(body: PostUnlikePramModel): Response<PostUnlikeResponse> {
        return getApi.onPostLike(body)
    }

    suspend fun onPostLiked(body: postLikedPramModel): Response<postLikedResponse> {
        return getApi.onPostLiked(body)
    }

    suspend fun onDeleteMyPost(body: deleteMyPostPramModel): Response<deleteMyPostResponse> {
        return getApi.onDeleteMyPost(body)
    }

    suspend fun onDeleteComment(body: deleteCommentPramModel): Response<deleteCommentResponse> {
        return getApi.onDeleteComment(body)
    }

    suspend fun onGetNotification(): Response<getNotificationResponse> {
        return getApi.onGetNotification()
    }

    suspend fun onNotificationRead(notification_id: String): Response<notificationReadResponse> {
        return getApi.onNotificationRead(notification_id)
    }

}