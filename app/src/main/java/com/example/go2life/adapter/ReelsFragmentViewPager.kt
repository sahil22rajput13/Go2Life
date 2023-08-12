package com.example.go2life.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.go2life.databinding.ItemsReelsFragmentBinding

class ReelsFragmentViewPager(
    private val context: Context,
) : RecyclerView.Adapter<ReelsFragmentViewPager.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemsReelsFragmentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(position: Int) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsReelsFragmentBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = 6

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(position)
    }
}
