package com.example.go2life.view.navigation.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.go2life.base.BaseFragment
import com.example.go2life.databinding.FragmentMoreBinding

class MoreFragment:BaseFragment(){
    lateinit var binding : FragmentMoreBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreBinding.inflate(layoutInflater)
        return binding.root
    }
}