package com.example.go2life.adapter.homeAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.go2life.databinding.ItemProfileViewpagerBinding
import com.example.go2life.model.homeData.home.Userpostgallerydata
import com.example.go2life.utils.gone
import com.example.go2life.utils.visible
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragmentViewPager(
    private val context: Context,
    val body: List<Userpostgallerydata>,
    val lifecycle: LifecycleCoroutineScope
) : RecyclerView.Adapter<HomeFragmentViewPager.ViewHolder>() {
    private var focusedVideoPosition: Int? = null
    private var isMuted = false

    inner class ViewHolder(val binding: ItemProfileViewpagerBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun setDataVideo(videoAct: Userpostgallerydata) = with(binding) {
            if (videoAct.file_type == 2) {
                videoView.setVideoPath(videoAct.file_url)
                ivProfile.visible()
                Glide.with(context).load(videoAct.video_thum).into(ivProfile)
                videoView.setOnPreparedListener { mediaPlayer ->
                    loadingIndicator.visibility = if (position == focusedVideoPosition) View.GONE else View.VISIBLE
                    ivProfile.gone()
                    mediaPlayer.start()
                    mediaPlayer.isLooping = true
                    if (absoluteAdapterPosition == focusedVideoPosition) {
                        focusedVideoPosition?.let {
                            notifyItemChanged(it)

                        }
                        focusedVideoPosition = absoluteAdapterPosition
                        notifyItemChanged(absoluteAdapterPosition)

                    }
                    binding.root.setOnClickListener {
                        if (mediaPlayer != null) {
                            isMuted = !isMuted
                            binding.soundView.visible()
                            val volume = if (isMuted){
                                0f
                            }  else 1f
                            mediaPlayer.setVolume(volume, volume)

                            val soundIconRes = if (isMuted) android.R.drawable.ic_lock_silent_mode else android.R.drawable.ic_lock_silent_mode_off
                            lifecycle.launch {
                                delay(4000)
                                binding.soundView.gone()
                            }
                            binding.soundView.setImageResource(soundIconRes)
                        }
                    }
                }
                videoView.setOnCompletionListener { mediaPlayer ->
                    mediaPlayer.start()
                }
            } else if (videoAct.file_type == 1) {
                ivProfile.visible()
                Glide.with(context).load(videoAct.file_url).into(ivProfile)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProfileViewpagerBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = body.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val body = body[position]
        holder.setDataVideo(body)
    }
}
