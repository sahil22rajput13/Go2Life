package com.example.go2life.view.navigation.home

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.go2life.R
import com.example.go2life.adapter.commentAdapter.CommentHomeAdapter
import com.example.go2life.adapter.commentAdapter.PostLikeUserListAdapter
import com.example.go2life.adapter.commentAdapter.UserCommentAdapter
import com.example.go2life.base.BaseFragment
import com.example.go2life.base.MyApplication
import com.example.go2life.databinding.LayoutBottomCommentAlertBinding
import com.example.go2life.databinding.LayoutBottomCommentsBinding
import com.example.go2life.model.comment.deleteComment.deleteCommentPramModel
import com.example.go2life.model.comment.postDetail.Body
import com.example.go2life.model.comment.postDetail.PostPramModel
import com.example.go2life.model.comment.postLikeUserList.postLikedPramModel
import com.example.go2life.model.comment.postlikeComment.postLikePramModel
import com.example.go2life.model.comment.postunlike.PostUnlikePramModel
import com.example.go2life.model.homeData.home.Usercomment
import com.example.go2life.network.Repository
import com.example.go2life.utils.Status.ERROR
import com.example.go2life.utils.Status.LOADING
import com.example.go2life.utils.Status.SUCCESS
import com.example.go2life.utils.inVisible
import com.example.go2life.utils.toast
import com.example.go2life.utils.visible
import com.example.go2life.viewmodels.HomeViewModel
import com.example.go2life.viewmodels.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CommentFragment : BaseFragment(), View.OnClickListener {
    lateinit var binding: LayoutBottomCommentsBinding
    private var postId: String = " "
    private lateinit var  bottomSheetDialog : BottomSheetDialog
    lateinit var bindingAlert: LayoutBottomCommentAlertBinding
    private lateinit var userCommentAdapter: UserCommentAdapter
    private lateinit var commentHomeAdapter: CommentHomeAdapter
    private lateinit var postLikeUserListAdapter: PostLikeUserListAdapter
    private var isLiked: Int = 0
    private var mUserComment = ArrayList<Usercomment>()
    private var mPostLikeUserList = ArrayList<com.example.go2life.model.comment.postLikeUserList.Body>()
    private var mHomeComment = ArrayList<Body>()

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory(
            application = Application(), repository = Repository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = LayoutBottomCommentsBinding.inflate(layoutInflater)
        binding.onClick = this
        setProfile()
        observer()
        likeSelected()
        commentSelected()
        return binding.root

    }

    private fun commentSelected() {
        binding.tvComments.setOnClickListener {
            with(binding) {
                tvLikes.setTextColor(ContextCompat.getColor(requireContext(), R.color.txt_grey))
                tvComments.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.txt_purple
                    )
                )
                tvLikesLines.inVisible()
                tvCommentsLines.visible()
                viewModel.onCommentHomePost(PostPramModel(postId))

            }
        }
    }

    private fun likeSelected() {
        binding.tvLikes.setOnClickListener {
            with(binding) {
                tvLikes.setTextColor(ContextCompat.getColor(requireContext(), R.color.txt_purple))
                tvComments.setTextColor(ContextCompat.getColor(requireContext(), R.color.txt_grey))
                tvCommentsLines.inVisible()
                tvLikesLines.visible()
                viewModel.onPostLikeUserList(postLikedPramModel(postId))
            }

        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged", "InflateParams")
    private fun observer() {
        viewModel.resultCommentPost.observe(viewLifecycleOwner) { data ->
            data?.let {
                when (data.status) {
                    SUCCESS -> {
                        viewLifecycleOwner.lifecycleScope.launch {
                            delay(500)
                            MyApplication.hideLoader()
                        }

                        if (binding.tvComments.isFocusable) {
                            binding.tvComments.isFocusable = false
                            mUserComment.clear()
                            mUserComment.addAll(it.data!!.body.usercomment)
                            userCommentAdapter()
                            mHomeComment.clear()
                            mHomeComment.addAll(listOf(it.data.body))
                            commentHomeAdapter()
                        } else {
                            mHomeComment.clear()
                            mHomeComment.addAll(listOf(it.data!!.body))
                            commentHomeAdapter()
                            mUserComment.clear()
                            mUserComment.addAll(it.data.body.usercomment)
                            userCommentAdapter()
                        }
                    }

                    LOADING -> {
                        if (binding.tvComments.isFocusable) {
                            binding.tvComments.isFocusable = false
                            MyApplication.showLoader(requireContext())

                        } else if (!binding.tvComments.isFocusable) {
                            binding.tvComments.isFocusable = true
                            MyApplication.hideLoader()
                        }
                    }

                    ERROR -> handleError(data.message.toString())
                }
            }
        }
        viewModel.resultPostLikeAndComment.observe(viewLifecycleOwner) { data ->
            data.let {
                when (data.status) {
                    SUCCESS -> {
                        if (it.data?.message == "Post Commented Successfully.") {
                            viewModel.onCommentHomePost(PostPramModel(postId))
                        } else if (binding.tvLikesLines.isVisible) {
                            viewModel.onPostLikeUserList(postLikedPramModel(postId))
                        }
                    }

                    LOADING -> {

                    }

                    ERROR -> {

                    }
                }
            }
        }
        viewModel.resultPostUnLike.observe(viewLifecycleOwner) { data ->
            data.let {
                when (data.status) {
                    SUCCESS -> {
                        if (binding.tvLikesLines.isVisible) {
                            viewModel.onPostLikeUserList(postLikedPramModel(postId))
                        }
                    }

                    LOADING -> {

                    }

                    ERROR -> {

                    }
                }
            }
        }

        viewModel.resultPostLikeUserList.observe(viewLifecycleOwner) { data ->
            data.let {
                when (data.status) {
                    SUCCESS -> {
                        // Adding data to the mLikes list
                        mPostLikeUserList.clear()
                        mPostLikeUserList.addAll(it.data!!.body)
                        postLikeUserList()

                    }

                    LOADING -> {

                    }

                    ERROR -> handleError(data.message.toString())
                }
            }
        }
        viewModel.resultDeleteComment.observe(viewLifecycleOwner) { data ->
            data.let {
                when (data.status) {
                    SUCCESS -> {
                        // Adding data to the mLikes list
                        if (it.message == "Comment delete successfully")
                        viewModel.onCommentHomePost(PostPramModel(postId))


                    }

                    LOADING -> {

                    }

                    ERROR -> handleError(data.message.toString())
                }
            }
        }
    }

    private fun commentHomeAdapter() {
        commentHomeAdapter = CommentHomeAdapter(
            requireContext(), mHomeComment,
            commentCallbacks
        )
        binding.rvHomeComment.adapter = commentHomeAdapter
    }

    private fun postLikeUserList() {
        postLikeUserListAdapter =
            PostLikeUserListAdapter(requireContext(), mPostLikeUserList)
        binding.rvCommentItem.adapter = postLikeUserListAdapter
    }

    private fun userCommentAdapter() {
        userCommentAdapter =
            UserCommentAdapter(requireContext(), mUserComment, callbackInfoMore)
        binding.rvCommentItem.adapter = userCommentAdapter
    }

    private val commentCallbacks = object :
        CommentHomeAdapter.CommentHomeAdapterCallbacks {
        override fun onClickLike(likeCount: Int, likeId: Int, isLiked: Int) {
            if (isLiked == 0) {
                viewModel.onPostLikeAndComment(postLikePramModel("0", likeId.toString(), "1"))

            } else if (isLiked == 1) {
                viewModel.onPostUnLike(PostUnlikePramModel("0", likeId.toString(), "1"))
            }
        }
    }


    private val callbackInfoMore = object : UserCommentAdapter.CommentPostedAdapterCallbacks {
        @SuppressLint("InflateParams")
        override fun onMoreInfoClicked(commentId: Int, postId: Int) {
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
               viewModel.onDeleteComment(deleteCommentPramModel(commentId.toString(),postId.toString(),"deleteComment"))
                bottomSheetDialog.dismiss()
            }

        }

    }

    private fun handleError(errorMessage: String) {
        MyApplication.hideLoader()
        requireContext().toast(errorMessage)
    }

    private fun setProfile() {
        postId = arguments?.getString("postId").toString()
        isLiked = arguments?.getInt("isLiked")!!
        viewModel.onCommentHomePost(PostPramModel(postId))
    }

    override fun onClick(v: View?) = with(binding) {
        ivArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        ivPostComment.setOnClickListener {
            val comment = binding.etPostComment.text.toString()
            if (binding.etPostComment.text.toString() >= "0") {
                viewModel.onPostLikeAndComment(postLikePramModel(comment, postId, "2"))
                binding.etPostComment.text.clear()
            }

        }
    }
}