package com.example.go2life.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.go2life.model.homeData.getReels.getReelsResponse
import com.example.go2life.model.reels.commentReels.commentReelsPramModel
import com.example.go2life.model.reels.commentReels.commentReelsResponse
import com.example.go2life.model.reels.getReelcomment.getReelcommentPramModel
import com.example.go2life.model.reels.getReelcomment.getReelcommentResponse
import com.example.go2life.model.reels.likeUnlikeReels.likeUnlikeReelsPramModel
import com.example.go2life.model.reels.likeUnlikeReels.likeUnlikeReelsResponse
import com.example.go2life.network.Repository
import com.example.go2life.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReelViewModel(val application: Application, val repository: Repository) : ViewModel() {
    val resultGetReels = MutableLiveData<Resource<getReelsResponse>>()
    val resultLikeUnlikeReels = MutableLiveData<Resource<likeUnlikeReelsResponse>>()
    val resultGetReelcomment = MutableLiveData<Resource<getReelcommentResponse>>()
    val resultCommentReels = MutableLiveData<Resource<commentReelsResponse>>()

    fun onLikeUnlikeReels(body: likeUnlikeReelsPramModel) {
        resultLikeUnlikeReels.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.onLikeUnlikeReels(body)
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body()?.code == 200) {
                            resultLikeUnlikeReels.value = Resource.success(
                                response.body(),
                                response.body()!!.message
                            )
                        } else {
                            resultLikeUnlikeReels.value =
                                Resource.error(null, response.body()?.message.toString())
                        }
                    } else {
                        resultLikeUnlikeReels.value =
                            Resource.error(null, response.body()?.message.toString())

                    }
                }
            } catch (t: Exception) {
                withContext(Dispatchers.Main) {
                    resultLikeUnlikeReels.value = Resource.error(null, t.message.toString())
                }
            }
        }
    }

    fun onGetReelcomment(body: getReelcommentPramModel) {
        resultGetReelcomment.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.onGetReelcomment(body)
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body()?.code == 200) {
                            resultGetReelcomment.value = Resource.success(
                                response.body(),
                                response.body()!!.message
                            )
                        } else {
                            resultGetReelcomment.value =
                                Resource.error(null, response.body()?.message.toString())
                        }
                    } else {
                        resultGetReelcomment.value =
                            Resource.error(null, response.body()?.message.toString())

                    }
                }
            } catch (t: Exception) {
                withContext(Dispatchers.Main) {
                    resultGetReelcomment.value = Resource.error(null, t.message.toString())
                }
            }
        }
    }

    fun onCommentReels(body: commentReelsPramModel) {
        resultCommentReels.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.onCommentReels(body)
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body()?.code == 200) {
                            resultCommentReels.value = Resource.success(
                                response.body(),
                                response.body()!!.message
                            )
                        } else {
                            resultCommentReels.value =
                                Resource.error(null, response.body()?.message.toString())
                        }
                    } else {
                        resultCommentReels.value =
                            Resource.error(null, response.body()?.message.toString())

                    }
                }
            } catch (t: Exception) {
                withContext(Dispatchers.Main) {
                    resultCommentReels.value = Resource.error(null, t.message.toString())
                }
            }
        }
    }

    fun onGetReels() {
        resultGetReels.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.onGetReels()
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body()?.code == 200) {
                            resultGetReels.value = Resource.success(
                                response.body(),
                                response.body()!!.message
                            )
                        } else {
                            resultGetReels.value =
                                Resource.error(null, response.body()?.message.toString())
                        }
                    } else {
                        resultGetReels.value =
                            Resource.error(null, response.body()?.message.toString())

                    }
                }
            } catch (t: Exception) {
                withContext(Dispatchers.Main) {
                    resultGetReels.value = Resource.error(null, t.message.toString())
                }
            }
        }
    }
}