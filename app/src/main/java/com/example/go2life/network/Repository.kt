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
//    suspend fun onHome(body: HomePramModel): Response<HomeResponse> {
//        return getApi.onHome(body)
//    }

}