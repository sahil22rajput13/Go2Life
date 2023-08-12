package com.example.go2life.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.go2life.utils.Resource
import com.example.go2life.model.city.CityPramModel
import com.example.go2life.model.city.CityResponse
import com.example.go2life.model.country.CountryResponse
import com.example.go2life.model.login.LoginPramModel
import com.example.go2life.model.login.LoginResponse
import com.example.go2life.model.profile.ProfilePramModel
import com.example.go2life.model.profile.ProfileResponse
import com.example.go2life.model.signup.SignUpPramModel
import com.example.go2life.model.signup.SignUpResponse
import com.example.go2life.network.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(val application: Application, val repository: Repository) : ViewModel() {

    val resultSignUp = MutableLiveData<Resource<SignUpResponse>>()
    val resultLogin = MutableLiveData<Resource<LoginResponse>>()
    val resultProfile = MutableLiveData<Resource<ProfileResponse>>()
    val resultCountry = MutableLiveData<Resource<CountryResponse>>()
    val resultCity = MutableLiveData<Resource<CityResponse>>()


    fun onCountry() {
        resultCountry.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.onCountry()
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body()?.code == 200) {
                            resultCountry.value = Resource.success(
                                response.body(),
                                response.body()!!.message.toString()
                            )
                        } else {
                            resultCountry.value =
                                Resource.error(null, response.body()?.message.toString())
                        }
                    } else {
                        resultCountry.value =
                            Resource.error(null, response.body()?.message.toString())

                    }
                }
            } catch (t: Exception) {
                withContext(Dispatchers.Main) {
                    resultCountry.value = Resource.error(null, t.message.toString())
                }
            }
        }
    }

    fun onLogin(body: LoginPramModel) {
        resultLogin.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.onLogin(body)
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body()?.code == 200) {
                            resultLogin.value = Resource.success(
                                response.body(),
                                response.body()!!.message.toString()
                            )
                        } else {
                            resultLogin.value =
                                Resource.error(null, response.body()?.message.toString())
                        }
                    } else {
                        resultLogin.value =
                            Resource.error(null, response.body()?.message.toString())

                    }
                }
            } catch (t: Exception) {
                withContext(Dispatchers.Main) {
                    resultLogin.value = Resource.error(null, t.message.toString())
                }
            }
        }
    }

    fun onCity(body: CityPramModel) {
        resultCity.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.onCity(body)
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body()?.code == 200) {
                            resultCity.value = Resource.success(
                                response.body(),
                                response.body()!!.message.toString()
                            )
                        } else {
                            resultCity.value =
                                Resource.error(null, response.body()?.message.toString())
                        }
                    } else {
                        resultCity.value =
                            Resource.error(null, response.body()?.message.toString())

                    }
                }
            } catch (t: Exception) {
                withContext(Dispatchers.Main) {
                    resultCity.value = Resource.error(null, t.message.toString())
                }
            }
        }
    }

    fun onProfile(body: ProfilePramModel) {
        resultProfile.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.onProfile(body)
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body()?.code == 200) {
                            resultProfile.value = Resource.success(
                                response.body(),
                                response.body()!!.message.toString()
                            )
                        } else {
                            resultProfile.value =
                                Resource.error(null, response.body()?.message.toString())
                        }
                    } else {
                        resultProfile.value =
                            Resource.error(null, response.body()?.message.toString())

                    }
                }
            } catch (t: Exception) {
                withContext(Dispatchers.Main) {
                    resultProfile.value = Resource.error(null, t.message.toString())
                }
            }
        }
    }

    fun onSignUp(body: SignUpPramModel) {
        resultSignUp.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.onSignUp(body)
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body()?.code == 200) {
                            resultSignUp.value = Resource.success(
                                response.body(),
                                response.body()!!.message.toString()
                            )
                        } else {
                            resultSignUp.value =
                                Resource.error(null, response.body()?.message.toString())
                        }
                    } else {
                        resultSignUp.value =
                            Resource.error(null, response.body()?.message.toString())

                    }
                }
            } catch (t: Exception) {
                withContext(Dispatchers.Main) {
                    resultSignUp.value = Resource.error(null, t.message.toString())
                }
            }
        }
    }


}