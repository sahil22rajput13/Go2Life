package com.example.go2life.viewmodels

import com.example.go2life.pagination.HomePagingSource
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.example.go2life.model.home.Body
import com.example.go2life.model.home.HomePramModel
import com.example.go2life.network.Repository

class HomeViewModel(val application: Application, val repository: Repository) : ViewModel() {

//    val resultHome = MutableLiveData<Resource<HomeResponse>>()
//    val resultLogin = MutableLiveData<Resource<LoginResponse>>()
//    val resultProfile = MutableLiveData<Resource<ProfileResponse>>()
//    val resultCountry = MutableLiveData<Resource<CountryResponse>>()
//    val resultCity = MutableLiveData<Resource<CityResponse>>()

    fun onHome(body: HomePramModel): LiveData<PagingData<Body>> {
        return  Pager(config = PagingConfig(pageSize = 10), pagingSourceFactory = {
            HomePagingSource(body.limit)
        }).liveData.cachedIn(viewModelScope)
    }
//    fun onHome(body: HomePramModel) {
//        resultHome.value = Resource.loading(null)
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val response = repository.onHome(body)
//                withContext(Dispatchers.Main) {
//                    if (response.code() == 200) {
//                        if (response.body()?.code == 200) {
//                            resultHome.value = Resource.success(
//                                response.body(),
//                                response.body()!!.message.toString()
//                            )
//                        } else {
//                            resultHome.value =
//                                Resource.error(null, response.body()?.message.toString())
//                        }
//                    } else {
//                        resultHome.value =
//                            Resource.error(null, response.body()?.message.toString())
//
//                    }
//                }
//            } catch (t: Exception) {
//                withContext(Dispatchers.Main) {
//                    resultHome.value = Resource.error(null, t.message.toString())
//                }
//            }
//        }
//    }

//    fun onLogin(body: LoginPramModel) {
//        resultLogin.value = Resource.loading(null)
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val response = repository.onLogin(body)
//                withContext(Dispatchers.Main) {
//                    if (response.code() == 200) {
//                        if (response.body()?.code == 200) {
//                            resultLogin.value = Resource.success(
//                                response.body(),
//                                response.body()!!.message.toString()
//                            )
//                        } else {
//                            resultLogin.value =
//                                Resource.error(null, response.body()?.message.toString())
//                        }
//                    } else {
//                        resultLogin.value =
//                            Resource.error(null, response.body()?.message.toString())
//
//                    }
//                }
//            } catch (t: Exception) {
//                withContext(Dispatchers.Main) {
//                    resultLogin.value = Resource.error(null, t.message.toString())
//                }
//            }
//        }
//    }

//    fun onCity(body: CityPramModel) {
//        resultCity.value = Resource.loading(null)
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val response = repository.onCity(body)
//                withContext(Dispatchers.Main) {
//                    if (response.code() == 200) {
//                        if (response.body()?.code == 200) {
//                            resultCity.value = Resource.success(
//                                response.body(),
//                                response.body()!!.message.toString()
//                            )
//                        } else {
//                            resultCity.value =
//                                Resource.error(null, response.body()?.message.toString())
//                        }
//                    } else {
//                        resultCity.value =
//                            Resource.error(null, response.body()?.message.toString())
//
//                    }
//                }
//            } catch (t: Exception) {
//                withContext(Dispatchers.Main) {
//                    resultCity.value = Resource.error(null, t.message.toString())
//                }
//            }
//        }
//    }

//    fun onProfile(body: ProfilePramModel) {
//        resultProfile.value = Resource.loading(null)
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val response = repository.onProfile(body)
//                withContext(Dispatchers.Main) {
//                    if (response.code() == 200) {
//                        if (response.body()?.code == 200) {
//                            resultProfile.value = Resource.success(
//                                response.body(),
//                                response.body()!!.message.toString()
//                            )
//                        } else {
//                            resultProfile.value =
//                                Resource.error(null, response.body()?.message.toString())
//                        }
//                    } else {
//                        resultProfile.value =
//                            Resource.error(null, response.body()?.message.toString())
//
//                    }
//                }
//            } catch (t: Exception) {
//                withContext(Dispatchers.Main) {
//                    resultProfile.value = Resource.error(null, t.message.toString())
//                }
//            }
//        }
//    }

//    fun onSignUp(body: SignUpPramModel) {
//        resultSignUp.value = Resource.loading(null)
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val response = repository.onSignUp(body)
//                withContext(Dispatchers.Main) {
//                    if (response.code() == 200) {
//                        if (response.body()?.code == 200) {
//                            resultSignUp.value = Resource.success(
//                                response.body(),
//                                response.body()!!.message.toString()
//                            )
//                        } else {
//                            resultSignUp.value =
//                                Resource.error(null, response.body()?.message.toString())
//                        }
//                    } else {
//                        resultSignUp.value =
//                            Resource.error(null, response.body()?.message.toString())
//
//                    }
//                }
//            } catch (t: Exception) {
//                withContext(Dispatchers.Main) {
//                    resultSignUp.value = Resource.error(null, t.message.toString())
//                }
//            }
//        }
//    }


}