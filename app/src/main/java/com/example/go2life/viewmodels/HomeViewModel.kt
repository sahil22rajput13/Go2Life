package com.example.go2life.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.example.go2life.model.home.Body
import com.example.go2life.model.home.HomePramModel
import com.example.go2life.model.post.PostPramModel
import com.example.go2life.model.post.PostResponse
import com.example.go2life.model.postLiked.postLikedPramModel
import com.example.go2life.model.postLiked.postLikedResponse
import com.example.go2life.model.postlikeandcommnet.postLikePramModel
import com.example.go2life.model.postlikeandcommnet.postLikeResponse
import com.example.go2life.model.postunlike.PostUnlikePramModel
import com.example.go2life.model.postunlike.PostUnlikeResponse
import com.example.go2life.network.Repository
import com.example.go2life.pagination.HomePagingSource
import com.example.go2life.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(val application: Application, val repository: Repository) : ViewModel() {
    val resultPost = MutableLiveData<Resource<PostResponse>>()
    val resultPostLike = MutableLiveData<Resource<postLikeResponse>>()
    val resultPostLiked = MutableLiveData<Resource<postLikedResponse>>()
    val resultPostUnLike = MutableLiveData<Resource<PostUnlikeResponse>>()
//    val resultLogin = MutableLiveData<Resource<LoginResponse>>()
//    val resultProfile = MutableLiveData<Resource<ProfileResponse>>()
//    val resultCountry = MutableLiveData<Resource<CountryResponse>>()
//    val resultCity = MutableLiveData<Resource<CityResponse>>()


    fun onHome(body: HomePramModel): LiveData<PagingData<Body>> {
        return Pager(config = PagingConfig(pageSize = 10), pagingSourceFactory = {
            HomePagingSource(body.limit)
        }).liveData.cachedIn(viewModelScope)
    }

    fun onPost(body: PostPramModel) {
        resultPost.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.onPost(body)
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body()?.code == 200) {
                            resultPost.value = Resource.success(
                                response.body(),
                                response.body()!!.message.toString()
                            )
                        } else {
                            resultPost.value =
                                Resource.error(null, response.body()?.message.toString())
                        }
                    } else {
                        resultPost.value =
                            Resource.error(null, response.body()?.message.toString())

                    }
                }
            } catch (t: Exception) {
                withContext(Dispatchers.Main) {
                    resultPost.value = Resource.error(null, t.message.toString())
                }
            }
        }
    }

    fun onPostLike(body: postLikePramModel) {
        resultPostLike.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.onPostLike(body)
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body()?.code == 200) {
                            resultPostLike.value = Resource.success(
                                response.body(),
                                response.body()!!.message.toString()
                            )
                        } else {
                            resultPostLike.value =
                                Resource.error(null, response.body()?.message.toString())
                        }
                    } else {
                        resultPostLike.value =
                            Resource.error(null, response.body()?.message.toString())

                    }
                }
            } catch (t: Exception) {
                withContext(Dispatchers.Main) {
                    resultPostLike.value = Resource.error(null, t.message.toString())
                }
            }
        }
    }
    fun onPostLiked(body: postLikedPramModel) {
        resultPostLike.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.onPostLiked(body)
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body()?.code == 200) {
                            resultPostLiked.value = Resource.success(
                                response.body(),
                                response.body()!!.message.toString()
                            )
                        } else {
                            resultPostLiked.value =
                                Resource.error(null, response.body()?.message.toString())
                        }
                    } else {
                        resultPostLiked.value =
                            Resource.error(null, response.body()?.message.toString())

                    }
                }
            } catch (t: Exception) {
                withContext(Dispatchers.Main) {
                    resultPostLiked.value = Resource.error(null, t.message.toString())
                }
            }
        }
    }

    fun onPostUnLike(body: PostUnlikePramModel) {
        resultPostUnLike.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.onPostUnLike(body)
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body()?.code == 200) {
                            resultPostUnLike.value = Resource.success(
                                response.body(),
                                response.body()!!.message.toString()
                            )
                        } else {
                            resultPostUnLike.value =
                                Resource.error(null, response.body()?.message.toString())
                        }
                    } else {
                        resultPostUnLike.value =
                            Resource.error(null, response.body()?.message.toString())

                    }
                }
            } catch (t: Exception) {
                withContext(Dispatchers.Main) {
                    resultPostUnLike.value = Resource.error(null, t.message.toString())
                }
            }
        }
    }

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