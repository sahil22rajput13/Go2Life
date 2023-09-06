package com.example.go2life.adapter.reelsAdapter

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.go2life.R
import com.example.go2life.databinding.ItemsReelsFragmentBinding
import com.example.go2life.model.homeData.getReels.Body
import com.example.go2life.utils.gone
import com.example.go2life.utils.setImageToImageView
import com.example.go2life.utils.visible
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ReelsFragmentViewPager(
    val context: Context,
    private val body: ArrayList<Body>,
    val lifecycle: LifecycleCoroutineScope,
    val callbacks: ReelsFragmentViewPagerCallback
) : RecyclerView.Adapter<ReelsFragmentViewPager.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemsReelsFragmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val videoAct = body[position]
        holder.setDataVideo(videoAct)
//        <---------Broadcast Receiver--------->
        val filter = IntentFilter(BODY_LIST_CHANGED_ACTION)
        context.registerReceiver(holder.commentBroadcastReceiver, filter)

    }

    //    <---------Broadcast Receiver END--------->
//    <---------Broadcast Receiver--------->
    override fun onViewRecycled(holder: ViewHolder) {
        context.unregisterReceiver(holder.commentBroadcastReceiver)
        super.onViewRecycled(holder)
    }
//    <---------Broadcast Receiver END--------->


    override fun getItemCount(): Int {
        return body.size
    }

    inner class ViewHolder(val binding: ItemsReelsFragmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isMuted = false

        //                       <---------Broadcast Receiver--------->
        val commentBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == BODY_LIST_CHANGED_ACTION) {
                    val newSize = intent.getIntExtra("mListSize", -1)
                    if (newSize != -1) {
                        // Update your mList size here
                        // For example, you can update a TextView showing the size
                        binding.tvCommentReels.text = newSize.toString()
                    }
                }
            }
        }

        //        <---------Broadcast Receiver END--------->
        fun setDataVideo(videoAct: Body) = with(binding) {
            videoView.setVideoPath(videoAct.videourl)
            thumbnailImageView.visible()
            Glide.with(context).load(videoAct.userdata.profile_pic).placeholder(R.drawable.job_img1)
                .into(ivProfileReel)
            tvDescriptionReel.text = videoAct.description
            tvNameReel.text = videoAct.userdata.first_name
            setupLikeCommentCounts(videoAct)
            setupLikeButton(videoAct)
            Glide.with(context).load(videoAct.video_thum).into(thumbnailImageView)
            loadingIndicator.visible()
            ivCommentReels.setOnClickListener {
                callbacks.onCommentClick(
                    videoAct.id,
                    type = 0
                )
            }
            ivCameraView.setOnClickListener {
                callbacks.onCameraClick(videoAct.id)
            }
            videoView.setOnPreparedListener { mediaPlayer ->
                loadingIndicator.visibility = View.GONE
                thumbnailImageView.gone()
                mediaPlayer.start()
                binding.root.setOnClickListener {
                    if (mediaPlayer != null) {
                        isMuted = !isMuted
                        binding.soundView.visible()
                        val volume = if (isMuted) {
                            0f
                        } else 1f
                        mediaPlayer.setVolume(volume, volume)

                        val soundIconRes =
                            if (isMuted) android.R.drawable.ic_lock_silent_mode else android.R.drawable.ic_lock_silent_mode_off
                        lifecycle.launch {
                            delay(3000)
                            binding.soundView.gone()
                        }
                        binding.soundView.setImageResource(soundIconRes)
                    }
                }
//
            }
            videoView.setOnCompletionListener { mediaPlayer ->
                mediaPlayer.start()
            }
        }

        private fun setupLikeCommentCounts(body: Body) = with(binding) {
            if (body.likecount > 0) {
                tvLikeReels.text = body.likecount.toString().takeUnless { it.isNullOrEmpty() }
            } else {
                tvLikeReels.visibility = View.GONE
            }
            if (body.commentcount > 0) {
                tvCommentReels.text = body.commentcount.toString().takeUnless { it.isNullOrEmpty() }
            } else {
                tvCommentReels.visibility = View.GONE
            }
        }

        @SuppressLint("SetTextI18n")
        private fun setupLikeButton(body: Body) = with(binding) {
            val isLiked = body.is_liked
            val likeCount = body.likecount

            ivLikeReels.setOnClickListener {
                if (ivLikeReels.isFocusable) {
                    ivLikeReels.isFocusable = false
                    if (isLiked == 1) {
                        tvLikeReels.text = (likeCount).toString()
                    } else {
                        tvLikeReels.text = (likeCount + 1).toString()
                        if (tvLikeReels.text == "0") {
                            tvLikeReels.visibility = View.GONE
                        }
                        callbacks.onItemClick(
                            body.id,
                            type = 1
                        )

                    }
                    ivLikeReels.setImageResource(R.drawable.like_check)
                } else if (!ivLikeReels.isFocusable) {
                    ivLikeReels.isFocusable = true
                    if (tvLikeReels.text.toString().toInt() != 0) {
                        tvLikeReels.text = (tvLikeReels.text.toString().toInt() - 1).toString()
                        callbacks.onItemClick(
                            body.id,
                            type = 0
                        )
                    }
                    ivLikeReels.setImageResource(R.drawable.home_like_uncheck)
                }

            }

            setImageToImageView(
                context, ivLikeReels,
                if (isLiked == 0) R.drawable.home_like_uncheck else R.drawable.like_check
            )
        }
    }


    interface ReelsFragmentViewPagerCallback {
        fun onItemClick(id: Int, type: Int)
        fun onCommentClick(id: Int, type: Int)
        fun onCameraClick(id: Int)
    }

    companion object {
        const val BODY_LIST_CHANGED_ACTION = "com.example.go2life.NEW_COMMENT_POSTED"
    }


}