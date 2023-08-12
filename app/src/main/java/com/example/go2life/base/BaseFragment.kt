package com.example.go2life.base

import android.app.Application
import androidx.fragment.app.Fragment
import com.example.go2life.network.Repository

open class BaseFragment : Fragment() {
    companion object {
        val application = Application()
        val repository = Repository()


    }
}