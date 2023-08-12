package com.example.go2life.view.navigation.home

import android.annotation.SuppressLint
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
import com.example.go2life.databinding.LayoutBottomCommentsBinding
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


    object CallbackComment : HomeFragmentRecyclerView.RecyclerViewCommentCallback {
        override fun onItemClick(
            profilePic: String,
            firstName: String,
            location: String,
            companyName: String,
            createdAt: String
        ) {
            val bundle = Bundle()
            bundle.putString("profilePic", profilePic)
            bundle.putString("firstName", firstName)
            bundle.putString("location", location)
            bundle.putString("companyName", companyName)
            bundle.putString("createdAt", createdAt)

        }
    }

    private fun homeApi() {
        val adapter = HomeFragmentRecyclerView(requireContext(), CallbackComment)
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