package com.example.go2life.base

import com.example.go2life.utils.SharedPreference

object GetObjects {
    val preference = SharedPreference.getInstance(MyApplication.mContext)
}