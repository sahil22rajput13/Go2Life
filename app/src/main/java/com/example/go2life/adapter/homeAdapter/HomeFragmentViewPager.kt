package com.example.go2life.adapter.homeAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.go2life.databinding.ItemProfileViewpagerBinding
import com.example.go2life.model.homeData.home.Userpostgallerydata
import com.example.go2life.utils.gone
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragmentViewPager(
    private val context: Context,
    val body: List<Userpostgallerydata>,
    val lifecycle: LifecycleCoroutineScope
) : RecyclerView.Adapter<HomeFragmentViewPager.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemProfileViewpagerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var exoPlayer: ExoPlayer? = null
        private var playbackCount = 0
        private var shouldPauseVideo = false

        init {
            exoPlayer = ExoPlayer.Builder(context).build()
            binding.playerView.player = exoPlayer
            binding.playerView.useController = false
            binding.root.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    if (shouldPauseVideo) {
                        exoPlayer?.seekTo(0)
                        exoPlayer?.play()
                        shouldPauseVideo = false
                    }
                } else {
                    exoPlayer?.pause()
                    shouldPauseVideo = true
                }
            }
        }


        fun bindData(position: Int) {
            val currentItem = body[position]
            if (currentItem.file_type == 2) {
                lifecycle.launch {
                    binding.ivProfile.visibility = View.VISIBLE
                    binding.playerView.visibility = View.GONE
                    Glide.with(context).load(currentItem.file_url).into(binding.ivProfile)
                    initializeExoPlayer(currentItem.file_url,currentItem.video_thum)
                }

            } else if (currentItem.file_type == 1) {
                binding.ivProfile.visibility = View.VISIBLE
                binding.playerView.visibility = View.GONE
                Glide.with(context).load(currentItem.file_url).into(binding.ivProfile)
                releaseExoPlayer()
            }
        }

        private fun initializeExoPlayer(videoUrl: String, videoThum: String) {

            binding.playerView.player = exoPlayer
            binding.playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            val mediaItem = MediaItem.fromUri(videoUrl)
            exoPlayer?.setMediaItem(mediaItem)
            exoPlayer?.prepare()

            // Play video three times
            exoPlayer?.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    if (playbackState == Player.STATE_READY) {
                        exoPlayer?.play()
                    }
                    if (playbackCount <= 3) {
                        if (playbackState == Player.STATE_ENDED) {
                            exoPlayer?.seekTo(0)
                            playbackCount++
                        }

                    }
                }


                override fun onIsLoadingChanged(isLoading: Boolean) {
                    super.onIsLoadingChanged(isLoading)
                    if (isLoading) {
                        binding.playerView.visibility = View.VISIBLE
                        binding.loadingIndicator.visibility = View.VISIBLE
                        lifecycle.launch{
                            delay(1000)
                            binding.ivProfile.gone()
                            binding.loadingIndicator.visibility = View.GONE
                        }

                        exoPlayer?.play()
                    }
                }
            })

            exoPlayer?.playWhenReady = true // Start playback
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResume() {
            if (!shouldPauseVideo) {
                exoPlayer?.playWhenReady = true // Start playback when visible
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onPause() {
            if (!shouldPauseVideo) {
                exoPlayer?.playWhenReady = false // Pause playback when not visible
            }
        }
        fun releaseExoPlayer() {
            exoPlayer?.stop()
            exoPlayer?.release()
            playbackCount = 0 // Reset playback count
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProfileViewpagerBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = body.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.releaseExoPlayer()
    }
}
