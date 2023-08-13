package com.example.go2life.view.navigation.home

import android.app.Application
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.go2life.R
import com.example.go2life.databinding.LayoutBottomCommentsBinding
import com.example.go2life.network.Repository
import com.example.go2life.utils.divToMonthsAndDays
import com.example.go2life.utils.gone
import com.example.go2life.viewmodels.AuthViewModel
import com.example.go2life.viewmodels.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class BottomCommentDialog : BottomSheetDialogFragment(), View.OnClickListener {
    lateinit var binding: LayoutBottomCommentsBinding
    private var profilePic: String = " "
    private var firstName: String = " "
    private var seekercategoryname: String = " "
    private var location: String = " "
    private var companyName: String = " "
    private var createdAt: String = " "

    val viewModel by viewModels<AuthViewModel> {
        ViewModelFactory(
            application = Application(), repository = Repository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = LayoutBottomCommentsBinding.inflate(layoutInflater)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        binding = LayoutBottomCommentsBinding.inflate(inflater, container, false)
        binding.onClick = this
        return binding.root

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = true
        getProfile()
        setProfile()
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            isFitToContents = true
            state = BottomSheetBehavior.STATE_COLLAPSED

        }
    }

    private fun getProfile() {
        profilePic = arguments?.getString("profilePic")!!
        firstName = arguments?.getString("firstName")!!
        companyName = arguments?.getString("companyName").toString()
        seekercategoryname = arguments?.getString("seekercategoryname").toString()
        location = arguments?.getString("location")!!
        createdAt = arguments?.getString("createdAt")!!
    }

    private fun setProfile() {
        Glide.with(this).load(profilePic).into(binding.ivProfile)
        binding.tvName.text = firstName
        if (companyName == "null" && seekercategoryname == "null") {
            binding.tvGraphic.gone()
        } else if (companyName == "null" && !seekercategoryname.isNullOrEmpty()) {
            binding.tvGraphic.text = seekercategoryname
        } else if (!companyName.isNullOrEmpty() && seekercategoryname == "null") {
            binding.tvGraphic.text = companyName
        }

        if (location.isNullOrEmpty()) {
            binding.tvLocation.gone()
        } else {
            binding.tvLocation.text = location
        }

        getDateAndTime(createdAt)
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

    private fun getDateAndTime(createdAt: String) {
        val created = createdAt
        val currentTime = System.currentTimeMillis()

        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        format.timeZone = TimeZone.getTimeZone("UTC")
        val createdAtDate = format.parse(created)
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
}