package com.example.go2life.view.navigation.market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.go2life.adapter.MarketFragmentRecyclerView
import com.example.go2life.base.BaseFragment
import com.example.go2life.databinding.FragmentMarketBinding

class MarketFragment : BaseFragment() {
    lateinit var binding: FragmentMarketBinding
    var marketFragmentRecyclerView: MarketFragmentRecyclerView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMarketBinding.inflate(layoutInflater)
        adapterSetup()
        return binding.root

    }

    private fun adapterSetup() {
        marketFragmentRecyclerView = MarketFragmentRecyclerView(requireContext())
        binding.rvMarket.adapter =marketFragmentRecyclerView
    }

}