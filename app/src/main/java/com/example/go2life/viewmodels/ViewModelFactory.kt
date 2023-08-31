package com.example.go2life.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.go2life.network.Repository

class ViewModelFactory(
    private val application: Application,
    private val repository: Repository
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                return AuthViewModel(application, repository) as T
            }

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                return HomeViewModel(application, repository) as T
            }

            modelClass.isAssignableFrom(ReelViewModel::class.java) -> {
                return ReelViewModel(application, repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }


    }

}