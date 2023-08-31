package com.example.go2life.view.navigation.reels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.go2life.R
import com.example.go2life.adapter.reelsAdapter.ReelsCommentAdapter
import com.example.go2life.databinding.LayoutBottomReelCommentBinding
import com.example.go2life.model.reels.commentReels.commentReelsPramModel
import com.example.go2life.model.reels.getReelcomment.getReelcommentPramModel
import com.example.go2life.network.Repository
import com.example.go2life.utils.Status.ERROR
import com.example.go2life.utils.Status.LOADING
import com.example.go2life.utils.Status.SUCCESS
import com.example.go2life.utils.toast
import com.example.go2life.viewmodels.ReelViewModel
import com.example.go2life.viewmodels.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ReelsCommentDialog : BottomSheetDialogFragment(), View.OnClickListener {
    lateinit var binding: LayoutBottomReelCommentBinding
    private lateinit var reelsCommentAdapter: ReelsCommentAdapter
    private var reelID: Int = 0
    private val mList: ArrayList<com.example.go2life.model.reels.getReelcomment.Body> =
        ArrayList()

    val viewModel by viewModels<ReelViewModel> {
        ViewModelFactory(
            application = Application(),
            repository = Repository()
        )
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = LayoutBottomReelCommentBinding.inflate(layoutInflater)
        getReelId()
        viewModel.onGetReelcomment(getReelcommentPramModel("0", reelID.toString(), "0"))
        observer()
        postComment()
        return binding.root

    }

    private fun postComment() {
        binding.tvPostComment.setOnClickListener {
            viewModel.onCommentReels(
                commentReelsPramModel(
                    binding.etReelComment.text.toString(),
                    reelID.toString(),
                    "0"
                )
            )
            binding.etReelComment.text?.clear()
            requireContext().toast(mList.size.toString())
            // After posting the comment, send a broadcast to notify the receiver

        }
    }


    private fun getReelId() {
        reelID = arguments?.getInt("reelId")!!
    }

    private fun observer() {
        viewModel.resultGetReelcomment.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                SUCCESS -> {
                    mList.clear()
                    mList.addAll(result.data?.body.orEmpty())
                    mList.size.toString()
                    requireContext().toast(mList.size.toString())
                    sortCommentedReelsList()
                    initCommentedReelsAdapter()

                    val intent = Intent("com.example.go2life.NEW_COMMENT_POSTED")
                    intent.putExtra("mListSize", mList.size)
                    requireContext().sendBroadcast(intent)
                    // Assuming you have a RecyclerView in your layout with id rvComments
                }

                LOADING -> {
                    // Handle loading state
                }

                ERROR -> {
                    // Handle error state
                }
            }
        }
        viewModel.resultCommentReels.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                SUCCESS -> {
                    viewModel.onGetReelcomment(getReelcommentPramModel("0", reelID.toString(), "0"))
                }

                LOADING -> {
                    // Handle loading state
                }

                ERROR -> {
                    // Handle error state
                }
            }
        }
    }

    private fun initCommentedReelsAdapter() {
        reelsCommentAdapter = ReelsCommentAdapter(
            requireContext(),
            mList,
            reelsCommentAdapterCallback
        )
        binding.rvCommentItem.adapter =
            reelsCommentAdapter
    }

    private val reelsCommentAdapterCallback =
        object : ReelsCommentAdapter.CommentPostedAdapterCallbacks {
            override fun onMoreInfoClicked(commentId: Int, postId: Int) {

            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = true
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            isFitToContents = true
            state = BottomSheetBehavior.STATE_EXPANDED

        }
    }

    override fun onClick(v: View?) {
    }

    private fun sortCommentedReelsList() {
        mList.sortByDescending { it.created_at }

    }

}