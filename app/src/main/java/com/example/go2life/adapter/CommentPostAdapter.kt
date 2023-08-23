package com.example.go2life.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.go2life.databinding.LayoutBottomCommentsBinding
import com.example.go2life.model.post.Body
import com.example.go2life.utils.toast

class CommentPostAdapter(
    private val context: Context,
    private val mList: List<Body>,
    private val callbacks: CommentPostAdapterCallbacks
) : RecyclerView.Adapter<CommentPostAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LayoutBottomCommentsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(body: Body) = with(binding) {

            setupCommentButton(body, binding)
        }
    }

    private fun setupCommentButton(body: Body, binding: LayoutBottomCommentsBinding) =
        with(binding) {
            ivPostComment.setOnClickListener {
                context.toast("click comment")
                callbacks.onClickComment(body.id)
            }
        }


    interface CommentPostAdapterCallbacks {
        fun onClickComment(postId: Int)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutBottomCommentsBinding.inflate(
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
