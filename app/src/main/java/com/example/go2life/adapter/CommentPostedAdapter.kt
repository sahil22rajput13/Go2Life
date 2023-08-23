package com.example.go2life.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.go2life.R
import com.example.go2life.databinding.ItemBottomCommentsBinding
import com.example.go2life.model.home.Usercomment
import com.example.go2life.utils.gone
import com.example.go2life.utils.visible
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommentPostedAdapter(
    private val context: Context,
    private val comments: List<Usercomment>
) : RecyclerView.Adapter<CommentPostedAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemBottomCommentsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(comment: Usercomment) = with(binding) {
            Glide.with(context)
                .load(comment.profilepic)
                .placeholder(R.drawable.job_img1)
                .fitCenter()
                .into(ivProfile)
            tvName.visible()
            tvName1.gone()
            tvName.text = comment.commentby
            clComment.visible()
            tvGraphic.text = comment.message
            tvDate1.text =
                formatDate(comment.created_at, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "dd MMM yyyy")
            tvTime1.text = formatDate(comment.created_at, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "HH:mm")
        }


        private fun formatDate(
            dateString: String?,
            inputFormat: String,
            outputFormat: String
        ): String = dateString?.let {
            val inputDateFormat = SimpleDateFormat(inputFormat, Locale.getDefault())
            val outputDateFormat = SimpleDateFormat(outputFormat, Locale.getDefault())

            val date: Date = inputDateFormat.parse(it) ?: Date()
            outputDateFormat.format(date)
        } ?: ""

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBottomCommentsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = comments.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]
        holder.bindData(comment)
    }
}
