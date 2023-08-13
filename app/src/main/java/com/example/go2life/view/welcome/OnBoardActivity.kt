package com.example.go2life.view.welcome

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.go2life.R
import com.example.go2life.adapter.OnBoardingAdapter
import com.example.go2life.base.GetObjects
import com.example.go2life.databinding.ActivityOnBoardBinding
import com.example.go2life.model.OnBoardModel
import com.example.go2life.utils.SharedPreference
import com.example.go2life.utils.inVisible
import com.example.go2life.utils.visible
import com.example.go2life.view.auth.LoginActivity

class OnBoardActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityOnBoardBinding
    val listData: ArrayList<OnBoardModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardBinding.inflate(layoutInflater)
        binding.onClick = this
        setData()
        spannableSkip()
        setViewpager()
        setContentView(binding.root)

    }


    private fun spannableSkip() {
        val mString = "Skip"
        val mSpannableString = SpannableString(mString)
        mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)
        binding.tvSkip.text = mSpannableString
    }

    private fun setData() {
        listData.clear()
        listData.add(
            OnBoardModel(
                R.drawable.onboarding1,
                "FIND BEST JOB SEEKERS",
                "Lorem Ipsum is simply dummy text of the \nprinting and typesetting industry. Lorem Ipsum \nhas been the industry's standard dummy"
            )
        )
        listData.add(
            OnBoardModel(
                R.drawable.onboarding2,
                "GREAT OPPORTUNITY",
                "Lorem Ipsum is simply dummy text of the \nprinting and typesetting industry. Lorem Ipsum \nhas been the industry's standard dummy"
            )
        )
        listData.add(
            OnBoardModel(
                R.drawable.onboarding3,
                "BEST IN MARKET",
                "Lorem Ipsum is simply dummy text of the \nprinting and typesetting industry. Lorem Ipsum \nhas been the industry's standard dummy"
            )
        )
    }

    private fun setViewpager() {
        binding.vpOnBoardViewPager.adapter = OnBoardingAdapter(this, listData)
        binding.vpOnBoardViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.onBoardViewedDots.attachToPager(binding.vpOnBoardViewPager)
        binding.vpOnBoardViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                spannableSkip()
                when (binding.vpOnBoardViewPager.currentItem) {

                    0 -> {
                        binding.tvSkip.visible()
                        binding.btnOnBoard.text = StringBuilder("Next")

                    }
                    1 -> {
                        binding.tvSkip.visible()
                        binding.btnOnBoard.text = StringBuilder("Next")

                    }
                    2 -> {
                        binding.btnOnBoard.text = StringBuilder("Start")
                        binding.tvSkip.inVisible()
                    }

                }
            }
        })


    }

    @SuppressLint("SetTextI18n")
    override fun onClick(p0: View?) {


        when (p0) {
            binding.tvSkip -> {
                GetObjects.preference.putBoolean(SharedPreference.Key.ISLANDINGCOMPLETE, true)
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        when (binding.vpOnBoardViewPager.currentItem) {
            0 -> {
                binding.tvSkip.visible()
                binding.btnOnBoard.text = StringBuilder("Next")
                binding.vpOnBoardViewPager.setCurrentItem(1, true)


            }

            1 -> {
                binding.tvSkip.visible()
                if (binding.vpOnBoardViewPager.currentItem == 1) {
                    binding.btnOnBoard.text = StringBuilder("Start")
                }
                binding.vpOnBoardViewPager.setCurrentItem(2, true)

            }

            2 -> {
                GetObjects.preference.putBoolean(SharedPreference.Key.ISLANDINGCOMPLETE, true)
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

            }
        }
    }
}
