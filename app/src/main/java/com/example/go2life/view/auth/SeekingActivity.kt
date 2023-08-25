package com.example.go2life.view.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.go2life.base.BaseActivity
import com.example.go2life.base.GetObjects
import com.example.go2life.databinding.ActivitySeekingBinding
import com.example.go2life.utils.SharedPreference
import com.example.go2life.utils.toast

class SeekingActivity : BaseActivity(), View.OnClickListener {
    lateinit var binding: ActivitySeekingBinding
    var seeker: String = ""
    var city: String = ""
    var country: String = ""
    var code: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeekingBinding.inflate(layoutInflater)
        binding.onClick = this
        seekerCheck()
        initData()
        setContentView(binding.root)
    }

    private fun initData() {
        city = intent.getStringExtra("City").toString()
        code = intent.getStringExtra("Code").toString()
        country = intent.getStringExtra("Country").toString()
    }

    private fun seekerCheck() {
        binding.CheckYes.setOnCheckedChangeListener { _, b ->
            if (b) {
                binding.CheckYes.isChecked = true
                binding.CheckNo.isChecked = false
                seeker = "Yes"
            }
        }
        binding.CheckNo.setOnCheckedChangeListener { _, b ->
            if (b) {
                binding.CheckYes.isChecked = false
                binding.CheckNo.isChecked = true
                seeker = "No"
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.btnLogin -> {
                if (validation()) {

                    GetObjects.preference.putString(SharedPreference.Key.ISRED, "isRed")
                    if (binding.CheckYes.isChecked) {
                        val intent = Intent(this, JobSeekerActivity::class.java)
                        intent.putExtra("City", city)
                        intent.putExtra("Code", code)
                        intent.putExtra("Country", country)
                        startActivity(intent)
                    } else if (binding.CheckNo.isChecked) {
                        val intent = Intent(this, CompanyActivity::class.java)
                        intent.putExtra("City", city)
                        intent.putExtra("Code", code)
                        intent.putExtra("Country", country)
                        startActivity(intent)
                    }
                }


            }

            binding.ivLogin -> {
                onBackPressed()
            }
        }
    }

    private fun validation(): Boolean {
        var isValid = true
        if (!binding.CheckYes.isChecked) {
            if (!binding.CheckNo.isChecked) {
                isValid = false
                toast("Select Checkbox")
            }

        }

        return isValid
    }
}