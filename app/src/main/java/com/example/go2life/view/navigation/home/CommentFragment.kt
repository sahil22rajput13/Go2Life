package com.example.go2life.view.navigation.home

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.go2life.R
import com.example.go2life.adapter.CommentHomeAdapter
import com.example.go2life.adapter.CommentLikesAdapter
import com.example.go2life.adapter.CommentPostAdapter
import com.example.go2life.adapter.CommentPostedAdapter
import com.example.go2life.base.BaseFragment
import com.example.go2life.base.MyApplication
import com.example.go2life.databinding.LayoutBottomCommentsBinding
import com.example.go2life.model.post.Body
import com.example.go2life.model.post.PostPramModel
import com.example.go2life.model.postLiked.postLikedPramModel
import com.example.go2life.model.postlikeandcommnet.postLikePramModel
import com.example.go2life.model.postunlike.PostUnlikePramModel
import com.example.go2life.network.Repository
import com.example.go2life.utils.Status.ERROR
import com.example.go2life.utils.Status.LOADING
import com.example.go2life.utils.Status.SUCCESS
import com.example.go2life.utils.inVisible
import com.example.go2life.utils.openKeyboard
import com.example.go2life.utils.toast
import com.example.go2life.utils.visible
import com.example.go2life.viewmodels.HomeViewModel
import com.example.go2life.viewmodels.ViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CommentFragment : BaseFragment(), View.OnClickListener {
    lateinit var binding: LayoutBottomCommentsBinding
    private var postId: String = " "
    private lateinit var commentPostedAdapter: CommentPostedAdapter
    private lateinit var commentHomeAdapter: CommentHomeAdapter
    private lateinit var commentPostAdapter: CommentPostAdapter
    private lateinit var commentLikesAdapter: CommentLikesAdapter
    private var isLiked: Int = 0

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

    @SuppressLint("ResourceAsColor", "NotifyDataSetChanged")
    private fun commentSelected() {
        binding.tvComments.setOnClickListener {
            with(binding) {
                tvLikes.setTextColor(resources.getColor(R.color.txt_grey))
                tvComments.setTextColor(resources.getColor(R.color.txt_purple))
                tvLikesLines.inVisible()
                tvCommentsLines.visible()
                if (tvComments.isEnabled) {
                    viewModel.onPost(PostPramModel(postId))
                }
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun likeSelected() {
        binding.tvLikes.setOnClickListener {
            with(binding) {
                tvLikes.setTextColor(resources.getColor(R.color.txt_purple))
                tvComments.setTextColor(resources.getColor(R.color.txt_grey))
                tvCommentsLines.inVisible()
                tvLikesLines.visible()
                viewModel.onPostLiked(postLikedPramModel(postId))

            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear the FLAG_LAYOUT_NO_LIMITS window flag when the fragment is destroyed
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun observer() {
        viewModel.resultPostLike.observe(viewLifecycleOwner) { data ->
            data.let {
                when (data.status) {
                    SUCCESS -> {
                        commentPostedAdapter.notifyDataSetChanged()
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

                    }

                    LOADING -> {

                    }

                    ERROR -> {

                    }
                }
            }
        }
        viewModel.resultPost.observe(viewLifecycleOwner) { data ->
            data?.let {
                when (data.status) {
                    SUCCESS -> {
                        MyApplication.hideLoader()
                        commentHomeAdapter =
                            CommentHomeAdapter(
                                requireContext(),
                                mList = listOf(it.data!!.body),
                                commentCallbacks
                            )
                        binding.rvHomeComment.adapter = commentHomeAdapter
                        commentStatus(it.data.body)
                    }

                    LOADING -> {
                        if (binding.tvComments.isClickable && binding.tvComments.isFocusable) {
                            binding.tvComments.isFocusable = false
                            MyApplication.showLoader(requireContext())
                        } else if (!binding.tvComments.isFocusable) {
                            MyApplication.hideLoader()

                        }

                    }


                    ERROR -> handleError(data.message.toString())
                }
            }
        }
        viewModel.resultPostLiked.observe(viewLifecycleOwner) { data ->
            data.let {
                when (data.status) {
                    SUCCESS -> {
                        val bodyList =
                            data.data?.body // Replace with the actual list of Body objects
                        if (bodyList != null) {
                            commentLikesAdapter = CommentLikesAdapter(requireContext(), bodyList)
                            binding.rvCommentItem.adapter = commentLikesAdapter
                        }
                    }

                    LOADING -> MyApplication.showLoader(requireContext())
                    ERROR -> handleError(data.message.toString())
                }
            }
        }
    }


    private val commentCallbacks = object :
        CommentHomeAdapter.CommentHomeAdapterCallbacks {
        override fun onClickLike(likeCount: Int, likeId: Int, isLiked: Int) {
            if (isLiked == 0) {
                viewModel.onPostLike(postLikePramModel("0", likeId.toString(), "1"))
            } else if (isLiked == 1) {
                viewModel.onPostUnLike(PostUnlikePramModel("0", likeId.toString(), "1"))
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun commentStatus(body: Body) {
        commentPostedAdapter =
            CommentPostedAdapter(requireContext(), comments = body.usercomment)
        binding.rvCommentItem.adapter = commentPostedAdapter
        commentPostedAdapter.notifyDataSetChanged()
    }

    //    private fun likeAndUnlike() {
//        with(binding) {
//            llLikes.setOnClickListener {
//                if (isLiked == 0)
//                    viewModel.onPostLike(postLikePramModel("0", postId, "1"))
//                else if (isLiked == 1)
//                    viewModel.onPostUnLike(PostUnlikePramModel("0", postId, "1"))
//            }
//        }
//    }
    private fun handleError(errorMessage: String) {
        MyApplication.hideLoader()
        requireContext().toast(errorMessage)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        keyboardOpen()
    }

    private fun keyboardOpen() {
        binding.nsProfile.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            binding.root.getWindowVisibleDisplayFrame(rect)
            val screenHeight = binding.root.height
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) {
                // Keyboard is open
                val scrollAmount = binding.rlCommentSend.bottom - rect.bottom
                binding.nsProfile.smoothScrollTo(0, scrollAmount)
            } else {
                // Keyboard is closed
                binding.nsProfile.scrollTo(0, 500)
            }
        }
        openKeyboard(binding.etPostComment)
        openSoftKeyboard()
    }


    private fun openSoftKeyboard() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(500)
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

    }


    private fun setProfile() {
        postId = arguments?.getString("postId").toString()
        isLiked = arguments?.getInt("isLiked")!!
        viewModel.onPost(PostPramModel(postId))


    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(v: View?) = with(binding){
        ivPostComment.setOnClickListener {
            val comment = binding.etPostComment.text.toString()
            if (binding.etPostComment.text.toString() >= "0"){
                viewModel.onPostLike(postLikePramModel(comment, postId, "2"))
                commentPostedAdapter.notifyDataSetChanged()
                viewModel.onPost(com.example.go2life.model.post.PostPramModel(postId))
            }
            binding.etPostComment.text.clear()
        }
    }
}