package com.example.go2life.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.go2life.databinding.ItemProfileViewpagerBinding
import com.example.go2life.model.home.Userpostgallerydata

class HomeFragmentViewPager(
    private val context: Context,
    private val body: List<Userpostgallerydata>
) : RecyclerView.Adapter<HomeFragmentViewPager.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemProfileViewpagerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(position: Int) {
            Glide.with(context).load(body[position].file_url).into(binding.ivProfile)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProfileViewpagerBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = body.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(position)
    }
}
