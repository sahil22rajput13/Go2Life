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
import com.example.go2life.adapter.homeAdapter.HomeFragmentAdapter
import com.example.go2life.base.BaseFragment
import com.example.go2life.databinding.FragmentHomeBinding
import com.example.go2life.model.home.HomePramModel
import com.example.go2life.model.postDetail.PostPramModel
import com.example.go2life.model.postlikeComment.postLikePramModel
import com.example.go2life.model.postunlike.PostUnlikePramModel
import com.example.go2life.utils.Status.ERROR
import com.example.go2life.utils.Status.LOADING
import com.example.go2life.utils.Status.SUCCESS
import com.example.go2life.view.navigation.MainActivity
import com.example.go2life.viewmodels.HomeViewModel
import com.example.go2life.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment() {
    private var binding: FragmentHomeBinding? = null
    var postIdUser:Int = 0
    private lateinit var homeFragmentAdapter: HomeFragmentAdapter
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
        return binding!!.root
    }

    private fun setupRecyclerView() {
        homeFragmentAdapter = HomeFragmentAdapter(requireContext(), callbackHome)
        binding?.rvProfile?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvProfile?.adapter = homeFragmentAdapter
        viewModel.onHome(HomePramModel("105", "0"))
            .observe(viewLifecycleOwner) { pagingData ->
                viewLifecycleOwner.lifecycleScope.launch {
                    homeFragmentAdapter.submitData(pagingData)
                }
            }
    }

    private val callbackHome = object : HomeFragmentAdapter.HomeFragmentRecyclerViewCallback {
        override fun onItemCallback(postId: Int) {
            val bundle = Bundle().apply {
                putString("postId", postId.toString())
            }
            (requireActivity() as MainActivity).goneIcon()
            viewLifecycleOwner.lifecycleScope.launch {
                findNavController().navigate(R.id.action_homeFragment_to_commentFragment, bundle)
            }

        }

        override fun onItemClick(postId: Int, isLiked: Int) {
            viewLifecycleOwner.lifecycleScope.launch {
                if (isLiked == 0) {
                    postIdUser = postId
                    viewModel.onPostLikeAndComment(postLikePramModel("0", postId.toString(), "1"))
                }
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
        viewModel.resultPostLikeAndComment.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                SUCCESS -> {
                    viewModel.onCommentHomePost(PostPramModel(postIdUser.toString()))
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
                    viewModel.onCommentHomePost(PostPramModel(postIdUser.toString()))
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
