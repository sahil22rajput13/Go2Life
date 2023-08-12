package com.example.go2life.view.navigation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.go2life.adapter.HomeFragmentRecyclerView
import com.example.go2life.base.BaseFragment
import com.example.go2life.databinding.FragmentHomeBinding
import com.example.go2life.model.home.Body
import com.example.go2life.model.home.HomePramModel
import com.example.go2life.viewmodels.HomeViewModel
import com.example.go2life.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment() : BaseFragment() {
    lateinit var binding: FragmentHomeBinding

    val viewModel by viewModels<HomeViewModel> { ViewModelFactory(application, repository) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        homeApi()
        return binding.root
    }


    private fun homeApi() {
        val adapter = HomeFragmentRecyclerView(requireContext())
        binding.rvProfile.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProfile.adapter = adapter

        viewModel.onHome(HomePramModel("10", "0"))
            .observe(viewLifecycleOwner) { pagingData ->
                viewLifecycleOwner.lifecycleScope.launch {
                    adapter.submitData(pagingData)
                }
            }
    }
}