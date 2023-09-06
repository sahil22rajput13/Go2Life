package com.example.go2life.view.navigation.reels

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import com.example.go2life.base.BaseFragment
import com.example.go2life.databinding.FragmentPreviewReelsCameraBinding

class PreviewReelsCamera : BaseFragment() {
    lateinit var binding: FragmentPreviewReelsCameraBinding
    private lateinit var videoView: VideoView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPreviewReelsCameraBinding.inflate(layoutInflater)
        videoView = binding.videoView
        setupVideoUrl()
        return binding.root
    }

    private fun setupVideoUrl() {
        val videoFilePath = arguments?.getString("videoFile")
        val selectedVideoUri = arguments?.getString("selectedVideoUri")

        if (!videoFilePath.isNullOrEmpty()) {
            videoView.setVideoURI(Uri.parse(videoFilePath))
            videoView.setOnPreparedListener { mediaPlayer ->
                mediaPlayer.start()
            }
            videoView.setOnCompletionListener {
                videoView.setZOrderOnTop(true);
                videoView.start()
            }
            videoView.setOnErrorListener { _, what, extra ->
                when (what) {
                    MediaPlayer.MEDIA_ERROR_SERVER_DIED -> {
                        // Handle server issues
                        // You may release and reinitialize the MediaPlayer here if needed.
                    }
                }
                true // Return true to indicate that you have handled the error
            }
        }
        if (!selectedVideoUri.isNullOrEmpty()) {
            videoView.setVideoPath(selectedVideoUri)
            videoView.setOnPreparedListener { mediaPlayer ->
                mediaPlayer.start()
            }
            videoView.setOnCompletionListener {
                videoView.start()
            }
            videoView.setOnErrorListener { _, what, extra ->
                when (what) {
                    MediaPlayer.MEDIA_ERROR_SERVER_DIED -> {
                        // Handle server issues
                        // You may release and reinitialize the MediaPlayer here if needed.
                    }
                }
                true // Return true to indicate that you have handled the error
            }
        }
    }


}
