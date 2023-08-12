package com.example.go2life.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.go2life.databinding.ItemOnBoardingBinding
import com.example.go2life.model.OnBoardModel

class OnBoardingAdapter(
    val context: Context,
    val body: ArrayList<OnBoardModel>
) : RecyclerView.Adapter<OnBoardingAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemOnBoardingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binData(position: Int) {
            binding.vector.setImageResource(body[position].image)
            binding.findBestJ.text = body[position].Tittle
            binding.loremIpsum.text = body[position].subTittle

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemOnBoardingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return body.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binData(position)
    }
}