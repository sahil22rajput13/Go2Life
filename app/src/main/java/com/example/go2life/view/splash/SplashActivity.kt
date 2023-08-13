package com.example.go2life.view.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.go2life.R
import com.example.go2life.base.GetObjects
import com.example.go2life.databinding.ActivitySplashBinding
import com.example.go2life.utils.SharedPreference
import com.example.go2life.view.auth.DetailActivity
import com.example.go2life.view.auth.LoginActivity
import com.example.go2life.view.welcome.OnBoardActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch {
            delay(1000)
            if (GetObjects.preference.getBoolean(SharedPreference.Key.ISLANDINGCOMPLETE) == false) {
                startActivity(Intent(this@SplashActivity, OnBoardActivity::class.java))
                finishAffinity()
            } else if (GetObjects.preference.getString(SharedPreference.Key.POSTCODE) == "" && GetObjects.preference.getString(
                    SharedPreference.Key.TOKEN
                ) != "" && GetObjects.preference.getString(SharedPreference.Key.USERID) == ""
            ) {
                startActivity(Intent(this@SplashActivity, DetailActivity::class.java))
                finishAffinity()
            } else if (GetObjects.preference.getString(SharedPreference.Key.USERID) == ""
            ) {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finishAffinity()
            } else if (GetObjects.preference.getString(SharedPreference.Key.POSTCODE) == "") {
                startActivity(Intent(this@SplashActivity, DetailActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }
        }
    }
}