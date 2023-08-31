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
import androidx.recyclerview.widget.RecyclerView
import com.example.go2life.R
import com.example.go2life.adapter.homeAdapter.HomeFragmentAdapter
import com.example.go2life.base.BaseFragment
import com.example.go2life.databinding.FragmentHomeBinding
import com.example.go2life.databinding.LayoutBottomCommentAlertBinding
import com.example.go2life.model.comment.postDelete.deleteMyPostPramModel
import com.example.go2life.model.comment.postDetail.PostPramModel
import com.example.go2life.model.comment.postlikeComment.postLikePramModel
import com.example.go2life.model.comment.postunlike.PostUnlikePramModel
import com.example.go2life.model.homeData.home.HomePramModel
import com.example.go2life.utils.NotificationService
import com.example.go2life.utils.Status.ERROR
import com.example.go2life.utils.Status.LOADING
import com.example.go2life.utils.Status.SUCCESS
import com.example.go2life.view.navigation.MainActivity
import com.example.go2life.viewmodels.HomeViewModel
import com.example.go2life.viewmodels.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch


class HomeFragment : BaseFragment() {
    private var binding: FragmentHomeBinding? = null
    var postIdUser: Int = 0
    lateinit var bindingAlert: LayoutBottomCommentAlertBinding
    private lateinit var homeFragmentAdapter: HomeFragmentAdapter
    lateinit var notificationService: NotificationService
    lateinit var mRecyclerView:RecyclerView
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
        observe()
        getNotification()
        return binding!!.root
    }

    private fun getNotification() {
        binding?.tvNotification?.setOnClickListener {
         findNavController().navigate(R.id.action_homeFragment_to_notificationFragment)
            // Inside your activity or fragment
//            val notificationService = NotificationService(requireContext()) // 'this' is the context
//            notificationService.showNotification("Notification Title", "Notification Message")

        }
    }

    private fun setupRecyclerView() {
        homeFragmentAdapter = HomeFragmentAdapter(requireContext(), callbackHome,lifecycleScope)
        binding?.rvProfile?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvProfile?.adapter = homeFragmentAdapter
        viewModel.onHome(HomePramModel("10", "0"))
            .observe(viewLifecycleOwner) {
                viewLifecycleOwner.lifecycleScope.launch {
                    homeFragmentAdapter.submitData(it)
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
                } else if (isLiked == 1)
                    viewModel.onPostUnLike(PostUnlikePramModel("0", postId.toString(), "1"))

            }
        }

        @SuppressLint("InflateParams")
        override fun onDeleteComment(postId: Int) {
            val bottomSheetDialog = BottomSheetDialog(requireContext())
            val dialogView =
                layoutInflater.inflate(R.layout.layout_bottom_comment_alert, null)
            bindingAlert = LayoutBottomCommentAlertBinding.bind(dialogView)
            bottomSheetDialog.setContentView(dialogView)
            bottomSheetDialog.show()
            bindingAlert.tvCancel.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            bindingAlert.tvDelete.setOnClickListener {
                viewModel.onDeleteMyPost(
                    deleteMyPostPramModel(
                        postId.toString()
                    )
                )
                bottomSheetDialog.dismiss()
            }

        }
    }


    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).visibleIcon()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observe() {
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
        viewModel.resultPostUnLike.observe(viewLifecycleOwner)
        { result ->
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
        viewModel.resultDeleteMyPost.observe(viewLifecycleOwner)
        { result ->
            when (result.status) {
                SUCCESS -> {
                    homeFragmentAdapter.refresh()
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
