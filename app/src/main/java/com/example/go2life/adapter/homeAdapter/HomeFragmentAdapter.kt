package com.example.go2life.adapter.homeAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.go2life.R
import com.example.go2life.databinding.ItemHomeFragmentBinding
import com.example.go2life.model.home.Body
import com.example.go2life.model.home.Userdata
import com.example.go2life.model.home.Userpostgallerydata
import com.example.go2life.utils.divToMonthsAndDays
import com.example.go2life.utils.gone
import com.example.go2life.utils.setImageToImageView
import com.example.go2life.utils.visible
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class HomeFragmentAdapter(
    private val context: Context,
    private val callbacks: HomeFragmentRecyclerViewCallback
) :
    PagingDataAdapter<Body, HomeFragmentAdapter.ViewHolder>(MovieComparator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHomeFragmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { body ->
            holder.bindData(body)
        }
    }

    inner class ViewHolder(val binding: ItemHomeFragmentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
        fun bindData(body: Body) {
            with(binding) {
                setupProfileInfo(body.userdata)
                setupPostGallery(body.userpostgallerydata)
                setupLikeButton(body)
                setupName(body.userdata)
                setupLikeCommentCounts(body)
                setupDescription(body)
                setupGraphic(body)
                setupLocation(body)
                setupTime(body.created_at)

                llComments.setOnClickListener {
                    callbacks.onItemCallback(body.id)
                }
            }
        }

        private fun setupProfileInfo(profileData: Userdata) = with(binding) {
            Glide.with(context).load(profileData.profile_pic).into(ivProfile)
            tvName.apply {
                text = profileData.first_name.takeUnless { it.isNullOrEmpty() }
                visibility = if (profileData.first_name.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
        }

        private fun setupPostGallery(postGalleryData: List<Userpostgallerydata>) = with(binding) {
            if (postGalleryData.isNullOrEmpty()) vpProfile.gone() else vpProfile.apply {
                visible()
                adapter = HomeFragmentViewPager(context, postGalleryData)
                ViewedDots.attachToPager(vpProfile)
            }
        }

        @SuppressLint("SetTextI18n")
        private fun setupLikeButton(body: Body) = with(binding) {
            val isLiked = body.is_liked
            val likeCount = body.likecount

            ivLike.setOnClickListener {
                if (ivLike.isFocusable) {
                    ivLike.isFocusable = false
                    if (isLiked == 1) {
                        tvLike.text = (likeCount).toString()
                    } else {
                        tvLike.text = (likeCount + 1).toString()
                        callbacks.onItemClick(
                            body.id,
                            isLiked = 0
                        )

                    }
                    ivLike.setImageResource(R.drawable.like_check)
                } else if (!ivLike.isFocusable) {
                    ivLike.isFocusable = true
                    if (tvLike.text.toString().toInt() != 0) {
                        tvLike.text = (tvLike.text.toString().toInt() - 1).toString()
                        callbacks.onItemClick(
                            body.id,
                            isLiked = 1
                        )
                    }
                    ivLike.setImageResource(R.drawable.home_like_uncheck)
                }

            }

            setImageToImageView(
                context, ivLike,
                if (isLiked == 0) R.drawable.home_like_uncheck else R.drawable.like_check
            )
        }

private fun setupName(profileData: Userdata) = with(binding) {
    tvName.text = profileData.first_name.takeUnless { it.isNullOrEmpty() }
    tvName.visibility =
        if (profileData.first_name.isNullOrEmpty()) View.GONE else View.VISIBLE
}

private fun setupLikeCommentCounts(body: Body) = with(binding) {
    tvLike.text = body.likecount.toString().takeUnless { it.isNullOrEmpty() }
    tvComment.text = body.commentcount.toString().takeUnless { it.isNullOrEmpty() }
}

private fun setupDescription(body: Body) = with(binding) {
    val description = body.description ?: body.jobdata?.job_description ?: ""
    val formattedDescription =
        HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT)
    tvDesc.text = formattedDescription
    tvDesc.visibility = if (description.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
}

private fun setupGraphic(body: Body) = with(binding) {
    val graphicText = body.company_name ?: body.seekercategoryname ?: ""
    tvGraphic.text = graphicText
    tvGraphic.visibility = if (graphicText.isNullOrEmpty()) View.GONE else View.VISIBLE
}

private fun setupLocation(body: Body) = with(binding) {
    val location = body.location ?: ""
    tvLocation.text = location
    tvLocation.visibility = if (location.isNullOrEmpty()) View.GONE else View.VISIBLE
}

private fun setupTime(createdAt: String) = with(binding) {
    val createdAtDate = parseCreatedAtDate(createdAt)
    val formattedTime = formatTimeDifference(createdAtDate)
    tvTime.text = formattedTime
}

private fun parseCreatedAtDate(createdAt: String): Long {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("UTC")
    return format.parse(createdAt)?.time ?: 0L
}

private fun formatTimeDifference(createdAtDate: Long): String {
    val currentTime = System.currentTimeMillis()
    val timeDifferenceInMilliseconds = currentTime - createdAtDate
    val (months, days) = timeDifferenceInMilliseconds.divToMonthsAndDays()

    return when {
        months > 0 -> "$months months ago"
        days > 0 -> "$days days ago"
        timeDifferenceInMilliseconds >= DateUtils.DAY_IN_MILLIS -> "${timeDifferenceInMilliseconds / DateUtils.DAY_IN_MILLIS} days ago"
        timeDifferenceInMilliseconds >= DateUtils.HOUR_IN_MILLIS -> "${timeDifferenceInMilliseconds / DateUtils.HOUR_IN_MILLIS} hours ago"
        timeDifferenceInMilliseconds >= DateUtils.MINUTE_IN_MILLIS -> "${timeDifferenceInMilliseconds / DateUtils.MINUTE_IN_MILLIS} min ago"
        else -> "Just now"
    }
}
}

interface HomeFragmentRecyclerViewCallback {
    fun onItemCallback(postId: Int)
    fun onItemClick(postId: Int, isLiked: Int)
}

object MovieComparator : DiffUtil.ItemCallback<Body>() {
    override fun areItemsTheSame(oldItem: Body, newItem: Body): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Body, newItem: Body): Boolean {
        return oldItem == newItem
    }
}
}
