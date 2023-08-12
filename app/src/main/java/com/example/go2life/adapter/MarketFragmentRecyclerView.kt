package com.example.go2life.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.go2life.databinding.ItemMarketFragmentBinding

class MarketFragmentRecyclerView(
    val context: Context,
) : RecyclerView.Adapter<MarketFragmentRecyclerView.ViewHolder>() {
    inner class ViewHolder(val binding: ItemMarketFragmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(position: Int) {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMarketFragmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = 8

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(position)
    }
}