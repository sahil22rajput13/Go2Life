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
import com.example.go2life.model.postDetail.PostPramModel
import com.example.go2life.model.postDetail.PostResponse
import com.example.go2life.model.postLikeUserList.postLikedPramModel
import com.example.go2life.model.postLikeUserList.postLikedResponse
import com.example.go2life.model.postlikeComment.postLikePramModel
import com.example.go2life.model.postlikeComment.postLikeResponse
import com.example.go2life.model.postunlike.PostUnlikePramModel
import com.example.go2life.model.postunlike.PostUnlikeResponse
import com.example.go2life.network.Repository
import com.example.go2life.pagination.HomePagingSource
import com.example.go2life.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(val application: Application, val repository: Repository) : ViewModel() {
    val resultCommentPost = MutableLiveData<Resource<PostResponse>>()
    val resultPostLikeAndComment = MutableLiveData<Resource<postLikeResponse>>()
    val resultPostLikeUserList = MutableLiveData<Resource<postLikedResponse>>()
    val resultPostUnLike = MutableLiveData<Resource<PostUnlikeResponse>>()


    fun onHome(body: HomePramModel): LiveData<PagingData<Body>> {
        return Pager(config = PagingConfig(pageSize = 10), pagingSourceFactory = {
            HomePagingSource(body.limit)
        }).liveData.cachedIn(viewModelScope)
    }

    fun onCommentHomePost(body: PostPramModel) {
        resultCommentPost.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.onPost(body)
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body()?.code == 200) {
                            resultCommentPost.value = Resource.success(
                                response.body(),
                                response.body()!!.message.toString()
                            )
                        } else {
                            resultCommentPost.value =
                                Resource.error(null, response.body()?.message.toString())
                        }
                    } else {
                        resultCommentPost.value =
                            Resource.error(null, response.body()?.message.toString())

                    }
                }
            } catch (t: Exception) {
                withContext(Dispatchers.Main) {
                    resultCommentPost.value = Resource.error(null, t.message.toString())
                }
            }
        }
    }

    fun onPostLikeAndComment(body: postLikePramModel) {
        resultPostLikeAndComment.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.onPostLike(body)
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body()?.code == 200) {
                            resultPostLikeAndComment.value = Resource.success(
                                response.body(),
                                response.body()!!.message.toString()
                            )
                        } else {
                            resultPostLikeAndComment.value =
                                Resource.error(null, response.body()?.message.toString())
                        }
                    } else {
                        resultPostLikeAndComment.value =
                            Resource.error(null, response.body()?.message.toString())

                    }
                }
            } catch (t: Exception) {
                withContext(Dispatchers.Main) {
                    resultPostLikeAndComment.value = Resource.error(null, t.message.toString())
                }
            }
        }
    }

    fun onPostLikeUserList(body: postLikedPramModel) {
        resultPostLikeUserList.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.onPostLiked(body)
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body()?.code == 200) {
                            resultPostLikeUserList.value = Resource.success(
                                response.body(),
                                response.body()!!.message.toString()
                            )
                        } else {
                            resultPostLikeUserList.value =
                                Resource.error(null, response.body()?.message.toString())
                        }
                    } else {
                        resultPostLikeUserList.value =
                            Resource.error(null, response.body()?.message.toString())

                    }
                }
            } catch (t: Exception) {
                withContext(Dispatchers.Main) {
                    resultPostLikeUserList.value = Resource.error(null, t.message.toString())
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
}