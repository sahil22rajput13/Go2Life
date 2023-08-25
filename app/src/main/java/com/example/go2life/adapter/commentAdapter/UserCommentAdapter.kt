package com.example.go2life.adapter.commentAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.go2life.R
import com.example.go2life.databinding.ItemBottomCommentsBinding
import com.example.go2life.model.homeData.home.Usercomment
import com.example.go2life.utils.gone
import com.example.go2life.utils.visible
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserCommentAdapter(
    private val context: Context,
    private val comments: List<Usercomment>,
    val callbacks: CommentPostedAdapterCallbacks
) : RecyclerView.Adapter<UserCommentAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemBottomCommentsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(comment: Usercomment) {
            with(binding) {
                Glide.with(context)
                    .load(comment.profilepic)
                    .placeholder(R.drawable.job_img1)
                    .fitCenter()
                    .into(ivProfile)
                tvName.visible()
                tvNameCenter.gone()
                tvName.text = comment.commentby
                clComment.visible()
                tvGraphic.text = comment.message
                // Split the API date into date and time components
                val (formattedDate, formattedTime) = formatDate(
                    comment.created_at,
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
                )

                tvDate.text = formattedDate
                tvTime.text = formattedTime
                ivMoreInfo.setOnClickListener {
                    callbacks.onMoreInfoClicked(commentId = comment.id, postId = comment.post_id)
                }
            }
        }

        private fun formatDate(dateString: String?, inputFormat: String): Pair<String, String> {
            val inputDateFormat = SimpleDateFormat(inputFormat, Locale.getDefault())
            val outputDateFormat = SimpleDateFormat("yyyy MMM dd", Locale.getDefault())
            val outputTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val date = inputDateFormat.parse(dateString ?: "") ?: Date()

            val formattedDate = outputDateFormat.format(date)
            val formattedTime = outputTimeFormat.format(date)

            return Pair(formattedDate, formattedTime)
        }
    }

    interface CommentPostedAdapterCallbacks {
        fun onMoreInfoClicked(commentId: Int, postId: Int)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemBottomCommentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = comments.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]
        holder.bindData(comment)
    }
}
