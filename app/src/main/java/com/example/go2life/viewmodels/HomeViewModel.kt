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
import com.example.go2life.model.homeData.home.Body
import com.example.go2life.model.homeData.home.HomePramModel
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
    val resultDeleteMyPost = MutableLiveData<Resource<deleteMyPostResponse>>()
    val resultDeleteComment = MutableLiveData<Resource<deleteCommentResponse>>()
    val resultGetNotification = MutableLiveData<Resource<getNotificationResponse>>()
    val resultNotificationRead = MutableLiveData<Resource<notificationReadResponse>>()



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

    fun onDeleteMyPost(body: deleteMyPostPramModel) {
        resultDeleteMyPost.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.onDeleteMyPost(body)
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body()?.code == 200) {
                            resultDeleteMyPost.value = Resource.success(
                                response.body(),
                                response.body()!!.message.toString()
                            )
                        } else {
                            resultDeleteMyPost.value =
                                Resource.error(null, response.body()?.message.toString())
                        }
                    } else {
                        resultDeleteMyPost.value =
                            Resource.error(null, response.body()?.message.toString())

                    }
                }
            } catch (t: Exception) {
                withContext(Dispatchers.Main) {
                    resultDeleteMyPost.value = Resource.error(null, t.message.toString())
                }
            }
        }
    }

    fun onDeleteComment(body: deleteCommentPramModel) {
        resultDeleteComment.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.onDeleteComment(body)
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body()?.code == 200) {
                            resultDeleteComment.value = Resource.success(
                                response.body(),
                                response.body()!!.message.toString()
                            )
                        } else {
                            resultDeleteComment.value =
                                Resource.error(null, response.body()?.message.toString())
                        }
                    } else {
                        resultDeleteComment.value =
                            Resource.error(null, response.body()?.message.toString())

                    }
                }
            } catch (t: Exception) {
                withContext(Dispatchers.Main) {
                    resultDeleteComment.value = Resource.error(null, t.message.toString())
                }
            }
        }
    }

    fun onGetNotification() {
        resultGetNotification.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.onGetNotification()
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body()?.code == 200) {
                            resultGetNotification.value = Resource.success(
                                response.body(),
                                response.body()!!.message.toString()
                            )
                        } else {
                            resultGetNotification.value =
                                Resource.error(null, response.body()?.message.toString())
                        }
                    } else {
                        resultGetNotification.value =
                            Resource.error(null, response.body()?.message.toString())

                    }
                }
            } catch (t: Exception) {
                withContext(Dispatchers.Main) {
                    resultGetNotification.value = Resource.error(null, t.message.toString())
                }
            }
        }
    }

    fun onNotificationRead(notification_id: String) {
        resultNotificationRead.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.onNotificationRead(notification_id)
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body()?.code == 200) {
                            resultNotificationRead.value = Resource.success(
                                response.body(),
                                response.body()!!.message.toString()
                            )
                        } else {
                            resultNotificationRead.value =
                                Resource.error(null, response.body()?.message.toString())
                        }
                    } else {
                        resultNotificationRead.value =
                            Resource.error(null, response.body()?.message.toString())

                    }
                }
            } catch (t: Exception) {
                withContext(Dispatchers.Main) {
                    resultNotificationRead.value = Resource.error(null, t.message.toString())
                }
            }
        }
    }
}