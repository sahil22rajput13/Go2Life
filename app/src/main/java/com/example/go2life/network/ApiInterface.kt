package com.example.go2life.network

import com.example.go2life.model.city.CityPramModel
import com.example.go2life.model.city.CityResponse
import com.example.go2life.model.country.CountryResponse
import com.example.go2life.model.home.HomePramModel
import com.example.go2life.model.home.HomeResponse
import com.example.go2life.model.login.LoginPramModel
import com.example.go2life.model.login.LoginResponse
import com.example.go2life.model.profile.ProfilePramModel
import com.example.go2life.model.profile.ProfileResponse
import com.example.go2life.model.signup.SignUpPramModel
import com.example.go2life.model.signup.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

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
}