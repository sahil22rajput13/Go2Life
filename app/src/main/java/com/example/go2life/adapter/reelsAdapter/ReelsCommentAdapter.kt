package com.example.go2life.adapter.reelsAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.go2life.R
import com.example.go2life.databinding.ItemBottomReelCommentBinding
import com.example.go2life.model.reels.getReelcomment.Body
import com.example.go2life.utils.visible
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class ReelsCommentAdapter(
    private val context: Context,
    private val comments: ArrayList<Body>,
    val callbacks: CommentPostedAdapterCallbacks
) : RecyclerView.Adapter<ReelsCommentAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemBottomReelCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(comment: Body) {
            with(binding) {
                Glide.with(context)
                    .load(comment.profile_pic)
                    .placeholder(R.drawable.job_img1)
                    .fitCenter()
                    .into(ivProfile)
                tvName.visible()
                tvName.text = comment.firstname
                clComment.visible()
                tvGraphic.text = comment.comment
                tvDate.text = formatNotificationTime(comment.created_at)
                ivMoreInfo.setOnClickListener {
                    callbacks.onMoreInfoClicked(commentId = comment.id, postId = comment.id)
                }
            }
        }
        private fun formatNotificationTime(dateString: String?): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date = inputFormat.parse(dateString ?: "") ?: Date()
            val currentTime = System.currentTimeMillis()
            val elapsedTimeMillis = currentTime - date.time

            return when {
                elapsedTimeMillis < TimeUnit.MINUTES.toMillis(1) -> "Just now"
                elapsedTimeMillis < TimeUnit.HOURS.toMillis(1) -> {
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTimeMillis)
                    "$minutes ${if (minutes == 1L) "minute" else "minutes"} ago"
                }
                elapsedTimeMillis < TimeUnit.DAYS.toMillis(1) -> {
                    val hours = TimeUnit.MILLISECONDS.toHours(elapsedTimeMillis)
                    "$hours ${if (hours == 1L) "hour" else "hours"} ago"
                }
                elapsedTimeMillis < TimeUnit.DAYS.toMillis(30) -> {
                    val days = TimeUnit.MILLISECONDS.toDays(elapsedTimeMillis)
                    "$days ${if (days == 1L) "day" else "days"} ago"
                }
                elapsedTimeMillis < TimeUnit.DAYS.toMillis(365) -> {
                    val months = (TimeUnit.MILLISECONDS.toDays(elapsedTimeMillis) / 30).toInt()
                    "$months ${if (months == 1) "month" else "months"} ago"
                }
                else -> {
                    val years = (TimeUnit.MILLISECONDS.toDays(elapsedTimeMillis) / 365).toInt()
                    "$years ${if (years == 1) "year" else "years"} ago"
                }
            }
        }
    }

    interface CommentPostedAdapterCallbacks {
        fun onMoreInfoClicked(commentId: Int, postId: Int)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemBottomReelCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = comments.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]
        holder.bindData(comment)
    }
}
