package com.example.go2life.adapter.chatsAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.go2life.databinding.ItemChatsFragmentBinding

class ChatsFragmentRecyclerView(
    private val context: Context
) : RecyclerView.Adapter<ChatsFragmentRecyclerView.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemChatsFragmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(position: Int) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChatsFragmentBinding.inflate( LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = 20

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(position)
    }
}
