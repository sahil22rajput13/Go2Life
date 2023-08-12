package com.example.go2life.view.navigation.reels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.go2life.adapter.ReelsFragmentViewPager
import com.example.go2life.base.BaseFragment
import com.example.go2life.databinding.FragmentReelsBinding

class ReelsFragment : BaseFragment() {
    val reelsFragmentViewPager: ReelsFragmentViewPager? = null
    lateinit var binding: FragmentReelsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReelsBinding.inflate(layoutInflater)
        adapterSetup()
        return binding.root
    }

    private fun adapterSetup() {
        binding.vpReels.adapter = ReelsFragmentViewPager(requireContext())
    }
}