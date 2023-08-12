package com.example.go2life.adapter

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.go2life.R
import com.example.go2life.R.layout.layout_bottom_comments
import com.example.go2life.databinding.ItemHomeFragmentBinding
import com.example.go2life.databinding.LayoutBottomCommentsBinding
import com.example.go2life.model.home.Body
import com.example.go2life.utils.divToMonthsAndDays
import com.example.go2life.utils.gone
import com.example.go2life.utils.toast
import com.example.go2life.view.navigation.home.BottomCommentDialog
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class HomeFragmentRecyclerView(
    val context: Context,
    val callback: RecyclerViewCommentCallback
) : PagingDataAdapter<Body, RecyclerView.ViewHolder>(MovieComparator), View.OnClickListener {
    lateinit var bottomDialog1: LayoutBottomCommentsBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ItemHomeFragmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            getItem(position)?.let { body ->
                holder.bindData(body)
            }
        }
    }

    inner class ViewHolder(val binding: ItemHomeFragmentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(body: Body) {
            val bodyImg = body.userdata
            val homeFragmentViewPager = HomeFragmentViewPager(context, body.userpostgallerydata)
            binding.vpProfile.adapter = homeFragmentViewPager
            binding.ViewedDots.attachToPager(binding.vpProfile)
            Glide.with(context).load(bodyImg.profile_pic).into(binding.ivProfile)
            binding.tvName.text = bodyImg.first_name
            if (body.description.isNullOrEmpty()) {
                binding.tvDesc.gone()
            } else {
                binding.tvDesc.text = body.description
            }
            if (body.company_name.isNullOrEmpty()) {
                if (body.seekercategoryname.isNullOrEmpty()) {
                    binding.tvGraphic.gone()
                } else {
                    binding.tvGraphic.text = body.seekercategoryname
                }
            } else {
                binding.tvGraphic.text = body.company_name
            }
            if (body.location.isNullOrEmpty()) {
                binding.tvLocation.gone()
            } else {
                binding.tvLocation.text = body.location
            }
            getDateAndTime(body, binding)
            itemView.setOnClickListener {
                context.toast("click")
                val bottomDialog = BottomCommentDialog()
                bottomDialog.show(
                    (binding.root.context as AppCompatActivity).supportFragmentManager,
                    bottomDialog.tag
                )
                callback.onItemClick(
                    bodyImg.profile_pic,
                    bodyImg.first_name,
                    body.location,
                    body.company_name,
                    body.created_at,

                    )
            }

        }
    }

    interface RecyclerViewCommentCallback {
        fun onItemClick(
            profilePic: String,
            firstName: String,
            location: String,
            companyName: String,
            createdAt: String
        )
    }


    private fun getDateAndTime(body: Body, binding: ItemHomeFragmentBinding) {
        val createdAt = body.created_at
        val currentTime = System.currentTimeMillis()

        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        format.timeZone = TimeZone.getTimeZone("UTC")
        val createdAtDate = format.parse(createdAt)
        val timeDifferenceInMilliseconds = currentTime - createdAtDate!!.time
        val timeDifferenceInLong = currentTime - createdAtDate.time.toLong()
        val inSeconds = (timeDifferenceInMilliseconds / DateUtils.SECOND_IN_MILLIS).toInt()
        val inMinutes = (timeDifferenceInMilliseconds / DateUtils.MINUTE_IN_MILLIS).toInt()
        val hoursAgo = (timeDifferenceInMilliseconds / DateUtils.HOUR_IN_MILLIS).toInt()
        val inDays = (timeDifferenceInMilliseconds / DateUtils.DAY_IN_MILLIS).toInt()
        val (months, days) = timeDifferenceInLong.divToMonthsAndDays()

        val formattedTime = when {
            months > 0 -> "$months months ago"
            days > 0 -> "$days days ago"
            inDays > 0 -> "$inDays days ago"
            inSeconds < 1 -> "$inMinutes min ago"
            inMinutes < 1 -> "$inSeconds sec ago"
            else -> "$hoursAgo hrs ago"
        }
        binding.tvTime.text = formattedTime
    }

    object MovieComparator : DiffUtil.ItemCallback<Body>() {
        override fun areItemsTheSame(oldItem: Body, newItem: Body): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Body, newItem: Body): Boolean {
            return oldItem == newItem
        }
    }

    override fun onClick(v: View?) {

    }

}


