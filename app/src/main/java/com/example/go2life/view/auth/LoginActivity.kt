package com.example.go2life.view.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import com.example.go2life.base.BaseActivity
import com.example.go2life.base.GetObjects
import com.example.go2life.base.MyApplication
import com.example.go2life.databinding.ActivityLoginBinding
import com.example.go2life.model.auth.login.LoginPramModel
import com.example.go2life.utils.SharedPreference
import com.example.go2life.utils.Status.ERROR
import com.example.go2life.utils.Status.LOADING
import com.example.go2life.utils.Status.SUCCESS
import com.example.go2life.utils.Utility
import com.example.go2life.utils.setClickableAndUnderlinedText
import com.example.go2life.utils.toast
import com.example.go2life.view.navigation.MainActivity
import com.example.go2life.viewmodels.AuthViewModel
import com.example.go2life.viewmodels.ViewModelFactory
import java.util.regex.Pattern

class LoginActivity : BaseActivity(), View.OnClickListener {
    lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<AuthViewModel> { ViewModelFactory(application, repository) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        spannableText()
        observer()
        binding.onClick = this
        setContentView(binding.root)

    }

    private fun observer() {
        viewModel.resultLogin.observe(this) {
            it.let { data ->
                when (data.status) {
                    SUCCESS -> {
                        MyApplication.hideLoader()
                        val response = it?.data?.body
                        GetObjects.preference.putString(
                            SharedPreference.Key.TOKEN,
                            response!!.token
                        )
                        GetObjects.preference.putString(
                            SharedPreference.Key.USERDETIALS,
                            response.userDetails.toString()
                        )
                        GetObjects.preference.putString(
                            SharedPreference.Key.USERID,
                            response.userDetails.id.toString()
                        )
                        if (response.userDetails.city.isNullOrEmpty()) {
                            if (response.userDetails.county.toString().isNullOrEmpty())
                                startActivity(
                                    Intent(
                                        this@LoginActivity,
                                        DetailActivity::class.java
                                    )
                                )
                        } else if (response.userDetails.profile_pic.toString().isNullOrEmpty()) {
                            startActivity(Intent(this, JobSeekerActivity::class.java))
                        } else {
                            startActivity(Intent(this, MainActivity::class.java))
                            finishAffinity()
                        }
                    }

                    LOADING -> {
                        MyApplication.showLoader(this)
                    }

                    ERROR -> {
                        MyApplication.hideLoader()
                        toast(data.message.toString())
                    }
                }
            }
        }
    }

    private fun validation(): Boolean {
        var isValid = true
        if (TextUtils.isEmpty(binding.etEmail.text.toString())) {
            binding.etEmail.error = "Required Field"
        } else if (!Pattern.matches(
                Patterns.EMAIL_ADDRESS.toRegex().toString(), binding
                    .etEmail.text.toString()
            )
        ) {
            isValid = false
            binding.etEmail.error = "Enter a valid Email"
        }
        if (TextUtils.isEmpty(binding.etPassword.text.toString())) {
            isValid = false
            binding.etPassword.error = "Required Field"
        }
        return isValid
    }


    private fun spannableText() {

        binding.tvSignup.setClickableAndUnderlinedText(
            "Don't have any account ? Sign up",
            "Sign up",
            onClickAction = { startActivity(Intent(this, SignupActivity::class.java)) })
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.btnLogin -> {
                val deviceToken = Utility.getDeviceToken().toString()
                if (validation()) {
                    val email = binding.etEmail.text.toString()
                    val password = binding.etPassword.text.toString()
                    val body = LoginPramModel(
                        deviceToken,
                        deviceType = "android",
                        email,
                        password
                    )
                    viewModel.onLogin(body)
                }
            }
        }
    }
}