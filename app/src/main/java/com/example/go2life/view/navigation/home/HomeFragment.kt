package com.example.go2life.view.navigation.home


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.go2life.R
import com.example.go2life.adapter.HomeFragmentRecyclerView
import com.example.go2life.base.BaseFragment
import com.example.go2life.base.MyApplication
import com.example.go2life.databinding.FragmentHomeBinding
import com.example.go2life.model.home.HomePramModel
import com.example.go2life.model.postlikeandcommnet.postLikePramModel
import com.example.go2life.model.postunlike.PostUnlikePramModel
import com.example.go2life.utils.Status.ERROR
import com.example.go2life.utils.Status.LOADING
import com.example.go2life.utils.Status.SUCCESS
import com.example.go2life.view.navigation.MainActivity
import com.example.go2life.viewmodels.HomeViewModel
import com.example.go2life.viewmodels.ViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment() {
    private var binding: FragmentHomeBinding? = null
    private lateinit var homeFragmentRecyclerView: HomeFragmentRecyclerView
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory(application, repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        setupRecyclerView()
        observePostLikeResult()
        observePostUnLikeResult()
        MyApplication.showLoader(requireContext())
        return binding!!.root
    }

    private fun setupRecyclerView() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(800)
            MyApplication.hideLoader()
        }
        homeFragmentRecyclerView = HomeFragmentRecyclerView(requireContext(), callbackHome)
        binding?.rvProfile?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvProfile?.adapter = homeFragmentRecyclerView
        viewModel.onHome(HomePramModel("105", "0"))
            .observe(viewLifecycleOwner) { pagingData ->
                viewLifecycleOwner.lifecycleScope.launch {
                    homeFragmentRecyclerView.submitData(pagingData)

                }
            }
    }

    private val callbackHome = object : HomeFragmentRecyclerView.HomeFragmentRecyclerViewCallback {
        override fun onItemCallback(postId: Int) {
            val bundle = Bundle().apply {
                putString("postId", postId.toString())
            }
            (requireActivity() as MainActivity).goneIcon()
            findNavController().navigate(R.id.action_homeFragment_to_commentFragment, bundle)
        }

        override fun onItemClick(postId: Int, isLiked: Int) {
            viewLifecycleOwner.lifecycleScope.launch {
                if (isLiked == 0)
                    viewModel.onPostLike(postLikePramModel("0", postId.toString(), "1"))
                else if (isLiked == 1)
                    viewModel.onPostUnLike(PostUnlikePramModel("0", postId.toString(), "1"))

            }
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).visibleIcon()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observePostLikeResult() {
        viewModel.resultPostLike.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                SUCCESS -> {
                }

                LOADING -> { /* Handle loading case */
                }

                ERROR -> { /* Handle error case */
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observePostUnLikeResult() {
        viewModel.resultPostUnLike.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                SUCCESS -> {
                }

                LOADING -> { /* Handle loading case */
                }

                ERROR -> { /* Handle error case */
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}
