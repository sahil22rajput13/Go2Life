package com.example.go2life.base

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.example.go2life.network.Repository

open class BaseActivity : AppCompatActivity() {
    companion object {
        val application = Application()
        val repository = Repository()


    }


}