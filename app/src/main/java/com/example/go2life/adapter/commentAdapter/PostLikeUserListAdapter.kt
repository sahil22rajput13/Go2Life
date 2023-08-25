package com.example.go2life.adapter.commentAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.go2life.R
import com.example.go2life.databinding.ItemBottomCommentsBinding
import com.example.go2life.model.comment.postLikeUserList.Body
import com.example.go2life.utils.gone

class PostLikeUserListAdapter(
    private val context: Context,
    private val likes: ArrayList<Body>
) : RecyclerView.Adapter<PostLikeUserListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemBottomCommentsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(like: Body) {
            with(binding) {
                Glide.with(context)
                    .load(like.profilepic)
                    .placeholder(R.drawable.job_img1)
                    .fitCenter()
                    .into(ivProfile)

                tvName.gone()
                tvNameCenter.text = like.likedby
                clComment.gone()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBottomCommentsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return likes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val like = likes[position]
        holder.bindData(like)
    }
}
