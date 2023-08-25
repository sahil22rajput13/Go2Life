package com.example.go2life.adapter.homeAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.go2life.R
import com.example.go2life.databinding.ItemGetNotificationBinding
import com.example.go2life.model.homeData.getNotification.Body
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class GetNotificationAdapter(
    private val context: Context,
    private val body: ArrayList<Body>,
    val callbacks: GetNotificationAdapterCallbacks
) : RecyclerView.Adapter<GetNotificationAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemGetNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(body: Body) = with(binding) {
            val userData = body.usersenderdata
            Glide.with(context).load(userData.profile_pic).into(ivCircleImage)
            tvNotification.text = body.message
            tvTime.text = formatNotificationTime(body.created_at)
            if (body.is_read == 1){
                binding.clBackground.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            }
            binding.clBackground.setOnClickListener {
                callbacks.onItemClick(body.id)
            }
        }
    }

    interface GetNotificationAdapterCallbacks {
        fun onItemClick(body: Int)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGetNotificationBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = body.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(body[position])
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

