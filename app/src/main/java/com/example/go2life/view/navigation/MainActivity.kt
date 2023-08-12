package com.example.go2life.view.navigation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.go2life.utils.setTextColorToDefault
import com.example.go2life.utils.setTextColorToPink
import com.example.go2life.R
import com.example.go2life.databinding.ActivityMainBinding
import com.example.go2life.utils.setImageToImageView

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    lateinit var findNavHost: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        navSetup()
        defaultNav()
        binding.onClick = this
        setContentView(binding.root)
    }

    private fun defaultNav() {
        setTextColorToPink(binding.tvNavHome)
        setImageToImageView(this,binding.ivHome,R.drawable.home_check)
        navController.popBackStack(R.id.nav_home, true)
        navController.popBackStack(R.id.nav_market, true)
        navController.popBackStack(R.id.nav_jobs, true)
        navController.popBackStack(R.id.nav_reels, true)
        navController.popBackStack(R.id.nav_chats, true)
        navController.popBackStack(R.id.nav_more, true)
        navController.navigate(R.id.homeFragment)
    }

    private fun navSetup() {
        findNavHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = findNavHost.navController
    }


    override fun onClick(v: View?) {
        when (v) {
            binding.navHome -> {
                setImageToImageView(this,binding.ivHome,R.drawable.home_check)
                setImageToImageView(this,binding.ivMarket,R.drawable.marketplace_uncheck)
                setImageToImageView(this,binding.ivJobs,R.drawable.jobs_uncheck)
                setImageToImageView(this,binding.ivReels,R.drawable.reels_uncheck)
                setImageToImageView(this,binding.ivChats,R.drawable.chat_uncheck)
                setImageToImageView(this,binding.ivMore,R.drawable.more_uncheck)
                setTextColorToPink(binding.tvNavHome)
                setTextColorToDefault(binding.tvMarket)
                setTextColorToDefault(binding.tvJobs)
                setTextColorToDefault(binding.tvReels)
                setTextColorToDefault(binding.tvChats)
                setTextColorToDefault(binding.tvMore)
                navController.popBackStack(R.id.nav_home, true)
                navController.popBackStack(R.id.nav_market, true)
                navController.popBackStack(R.id.nav_jobs, true)
                navController.popBackStack(R.id.nav_reels, true)
                navController.popBackStack(R.id.nav_chats, true)
                navController.popBackStack(R.id.nav_more, true)
                navController.navigate(R.id.homeFragment)
            }

            binding.navMarket -> {
                setImageToImageView(this,binding.ivHome,R.drawable.home_uncheck)
                setImageToImageView(this,binding.ivMarket,R.drawable.marketplace_check)
                setImageToImageView(this,binding.ivJobs,R.drawable.jobs_uncheck)
                setImageToImageView(this,binding.ivReels,R.drawable.reels_uncheck)
                setImageToImageView(this,binding.ivChats,R.drawable.chat_uncheck)
                setImageToImageView(this,binding.ivMore,R.drawable.more_uncheck)
                setTextColorToPink(binding.tvMarket)
                setTextColorToDefault(binding.tvNavHome)
                setTextColorToDefault(binding.tvJobs)
                setTextColorToDefault(binding.tvReels)
                setTextColorToDefault(binding.tvChats)
                setTextColorToDefault(binding.tvMore)
                navController.popBackStack(R.id.nav_home, true)
                navController.popBackStack(R.id.nav_market, true)
                navController.popBackStack(R.id.nav_jobs, true)
                navController.popBackStack(R.id.nav_reels, true)
                navController.popBackStack(R.id.nav_chats, true)
                navController.popBackStack(R.id.nav_more, true)
                navController.navigate(R.id.marketFragment)
            }

            binding.navJobs -> {
                setImageToImageView(this,binding.ivHome,R.drawable.home_uncheck)
                setImageToImageView(this,binding.ivMarket,R.drawable.marketplace_uncheck)
                setImageToImageView(this,binding.ivJobs,R.drawable.jobs_check)
                setImageToImageView(this,binding.ivReels,R.drawable.reels_uncheck)
                setImageToImageView(this,binding.ivChats,R.drawable.chat_uncheck)
                setImageToImageView(this,binding.ivMore,R.drawable.more_uncheck)
                setTextColorToPink(binding.tvJobs)
                setTextColorToDefault(binding.tvMarket)
                setTextColorToDefault(binding.tvNavHome)
                setTextColorToDefault(binding.tvReels)
                setTextColorToDefault(binding.tvChats)
                setTextColorToDefault(binding.tvMore)
                navController.popBackStack(R.id.nav_home, true)
                navController.popBackStack(R.id.nav_market, true)
                navController.popBackStack(R.id.nav_jobs, true)
                navController.popBackStack(R.id.nav_reels, true)
                navController.popBackStack(R.id.nav_chats, true)
                navController.popBackStack(R.id.nav_more, true)
                navController.navigate(R.id.jobFragment)
            }

            binding.navReels -> {
                setImageToImageView(this,binding.ivHome,R.drawable.home_uncheck)
                setImageToImageView(this,binding.ivMarket,R.drawable.marketplace_uncheck)
                setImageToImageView(this,binding.ivJobs,R.drawable.jobs_uncheck)
                setImageToImageView(this,binding.ivReels,R.drawable.reels_check)
                setImageToImageView(this,binding.ivChats,R.drawable.chat_uncheck)
                setImageToImageView(this,binding.ivMore,R.drawable.more_uncheck)
                setTextColorToPink(binding.tvReels)
                setTextColorToDefault(binding.tvMarket)
                setTextColorToDefault(binding.tvJobs)
                setTextColorToDefault(binding.tvNavHome)
                setTextColorToDefault(binding.tvChats)
                setTextColorToDefault(binding.tvMore)

                navController.popBackStack(R.id.nav_home, true)
                navController.popBackStack(R.id.nav_market, true)
                navController.popBackStack(R.id.nav_jobs, true)
                navController.popBackStack(R.id.nav_reels, true)
                navController.popBackStack(R.id.nav_chats, true)
                navController.popBackStack(R.id.nav_more, true)
                navController.navigate(R.id.reelsFragment)
            }

            binding.navChats -> {
                setImageToImageView(this,binding.ivHome,R.drawable.home_uncheck)
                setImageToImageView(this,binding.ivMarket,R.drawable.marketplace_uncheck)
                setImageToImageView(this,binding.ivJobs,R.drawable.jobs_uncheck)
                setImageToImageView(this,binding.ivReels,R.drawable.reels_uncheck)
                setImageToImageView(this,binding.ivChats,R.drawable.chat_check)
                setImageToImageView(this,binding.ivMore,R.drawable.more_uncheck)
                setTextColorToPink(binding.tvChats)
                setTextColorToDefault(binding.tvMarket)
                setTextColorToDefault(binding.tvJobs)
                setTextColorToDefault(binding.tvReels)
                setTextColorToDefault(binding.tvNavHome)
                setTextColorToDefault(binding.tvMore)
                navController.popBackStack(R.id.nav_home, true)
                navController.popBackStack(R.id.nav_market, true)
                navController.popBackStack(R.id.nav_jobs, true)
                navController.popBackStack(R.id.nav_reels, true)
                navController.popBackStack(R.id.nav_chats, true)
                navController.popBackStack(R.id.nav_more, true)
                navController.navigate(R.id.chatsFragment)
            }

            binding.navMore -> {
                setTextColorToPink(binding.tvMore)
                setTextColorToDefault(binding.tvMarket)
                setTextColorToDefault(binding.tvJobs)
                setTextColorToDefault(binding.tvReels)
                setTextColorToDefault(binding.tvChats)
                setTextColorToDefault(binding.tvNavHome)
                setImageToImageView(this,binding.ivHome,R.drawable.home_uncheck)
                setImageToImageView(this,binding.ivMarket,R.drawable.marketplace_uncheck)
                setImageToImageView(this,binding.ivJobs,R.drawable.jobs_uncheck)
                setImageToImageView(this,binding.ivReels,R.drawable.reels_uncheck)
                setImageToImageView(this,binding.ivChats,R.drawable.chat_uncheck)
                setImageToImageView(this,binding.ivMore,R.drawable.more_check)

                navController.popBackStack(R.id.nav_home, true)
                navController.popBackStack(R.id.nav_market, true)
                navController.popBackStack(R.id.nav_jobs, true)
                navController.popBackStack(R.id.nav_reels, true)
                navController.popBackStack(R.id.nav_chats, true)
                navController.popBackStack(R.id.nav_more, true)
                navController.navigate(R.id.moreFragment)
            }
        }
    }
}
