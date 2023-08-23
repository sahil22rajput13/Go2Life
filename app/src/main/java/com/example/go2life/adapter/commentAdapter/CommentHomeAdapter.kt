package com.example.go2life.adapter.commentAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.go2life.R
import com.example.go2life.databinding.ItemHomeCommentsBinding
import com.example.go2life.model.postDetail.Body
import com.example.go2life.utils.divToMonthsAndDays
import com.example.go2life.utils.setImageToImageView
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class CommentHomeAdapter(
    private val context: Context,
    private val mList: List<Body>,
    private val callbacks: CommentHomeAdapterCallbacks
) : RecyclerView.Adapter<CommentHomeAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemHomeCommentsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(body: Body) = with(binding) {
            val bodyImg = body.userdata
            val createdAtDate = parseCreatedAtDate(body.created_at)


            // Set up ViewPager
            vpProfile.visibility =
                if (body.userpostgallerydata.isNullOrEmpty()) View.GONE else View.VISIBLE
            vpProfile.adapter = CommentFragmentViewPager(context, body.userpostgallerydata)
            ViewedDots.attachToPager(vpProfile)

            // Load user profile image
            Glide.with(context)
                .load(bodyImg.profile_pic)
                .placeholder(R.drawable.check_filled)
                .into(ivProfile)
            //Set up name setupLikeButton
            setupLikeButton(body, binding)
            // Set up name visibility and text
            tvName.visibility = if (bodyImg.first_name.isNullOrEmpty()) View.GONE else View.VISIBLE
            tvName.text = bodyImg.first_name


            if (body.commentcount >= 0) {
                tvComment.text = body.commentcount.toString().takeUnless { it.isNullOrEmpty() }
            }else {
                tvComment.visibility = View.GONE
            }
            tvDesc.text =
                body.description.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_COMPACT) }
            tvDesc.visibility =
                if (body.description.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
            tvGraphic.text =
                if (body.company_name.isNullOrEmpty()) body.seekercategoryname else body.company_name
            tvGraphic.visibility = if (tvGraphic.text.isNullOrEmpty()) View.GONE else View.VISIBLE
            tvLocation.text = body.location
            tvLocation.visibility = if (body.location.isNullOrEmpty()) View.GONE else View.VISIBLE
            tvTime.text = formatTimeDifference(createdAtDate)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setupLikeButton(body: Body, binding: ItemHomeCommentsBinding) = with(binding) {
        val isLiked = body.is_liked
        val likeCount = body.likecount
        tvLike.text = likeCount.toString()

        ivLike.setOnClickListener {
            if (!ivLike.isFocusable) {
                ivLike.isFocusable = true
                tvLike.text = (tvLike.text.toString().toInt() - 1).toString()
                callbacks.onClickLike(body.likecount, body.id, isLiked = 1)
                ivLike.setImageResource(R.drawable.home_like_uncheck)
            } else {
                ivLike.isFocusable = false
                if (isLiked == 1) {
                    tvLike.text = (likeCount).toString()
                } else {
                    tvLike.text = (likeCount + 1).toString()
                    callbacks.onClickLike(body.likecount, body.id, isLiked = 0)
                }
                ivLike.setImageResource(R.drawable.like_check)
            }
        }

        setImageToImageView(
            context, ivLike,
            if (isLiked == 0) R.drawable.home_like_uncheck else R.drawable.like_check
        )
    }

    interface CommentHomeAdapterCallbacks {
        fun onClickLike(likeCount: Int, likeId: Int, isLiked: Int)

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeCommentsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val body = mList[position]
        holder.bindData(body)
    }
}
