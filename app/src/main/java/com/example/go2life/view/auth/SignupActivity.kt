package com.example.go2life.view.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import com.example.go2life.viewmodels.ViewModelFactory
import com.example.go2life.base.BaseActivity
import com.example.go2life.base.GetObjects
import com.example.go2life.base.MyApplication
import com.example.go2life.databinding.ActivitySignupBinding
import com.example.go2life.model.signup.SignUpPramModel
import com.example.go2life.utils.SharedPreference
import com.example.go2life.utils.Status
import com.example.go2life.utils.setClickableAndUnderlinedText
import com.example.go2life.utils.toast
import com.example.go2life.viewmodels.AuthViewModel
import java.util.regex.Pattern

class SignupActivity : BaseActivity(), View.OnClickListener {
    val viewModel by viewModels<AuthViewModel> { ViewModelFactory(application, repository) }
    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignupBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        spannableText()
        observer()
        binding.onClick = this
        setContentView(binding.root)

    }

    private fun observer() {
        viewModel.resultSignUp.observe(this) {
            it.let { data ->
                when (data.status) {
                    Status.SUCCESS -> {
                        MyApplication.hideLoader()
                        val response = it.data?.body
                        GetObjects.preference.putString(
                            SharedPreference.Key.TOKEN,
                            response?.token.toString()
                        )
                        GetObjects.preference.putString(
                            SharedPreference.Key.USERDETIALS,
                            response?.userDetails.toString()
                        )
                        startActivity(Intent(this, DetailActivity::class.java))
                    }

                    Status.LOADING -> {
                        MyApplication.showLoader(this)
                    }

                    Status.ERROR -> {
                        MyApplication.hideLoader()
                        toast(data.message.toString())
                    }
                }
            }
        }
    }

    private fun spannableText() {
        binding.tvLogin.setClickableAndUnderlinedText(
            "Already have an account ? Login",
            " Login",
            onClickAction = {
                startActivity(
                    Intent(this, LoginActivity::class.java)
                )
            })
        binding.tvTerms.setClickableAndUnderlinedText(
            "I accept all Terms & Conditions",
            "Terms & Conditions",
            onClickAction = {
                startActivity(
                    Intent(this, LoginActivity::class.java)
                )
            })
    }

    private fun validation(): Boolean {
        var isValid = true

        if (TextUtils.isEmpty(binding.etName.text.toString())) {
            isValid = false
            binding.etName.error = "Required Field"
        }
        if (TextUtils.isEmpty(binding.etEmail.text.toString())) {
            isValid = false
            binding.etEmail.error = "Required Field"
        } else if (!Pattern.matches(
                Patterns.EMAIL_ADDRESS.toRegex().toString(), binding
                    .etEmail.text.toString()
            )
        ) {
            isValid = false
            binding.etEmail.error = "Enter a valid Email"
        }
        if (TextUtils.isEmpty(binding.etUser.text.toString())) {
            isValid = false
            binding.etUser.error = "Required Field"
        }
        if (TextUtils.isEmpty(binding.etMobile.text.toString())) {
            isValid = false
            binding.etMobile.error = "Required Field"
        }
        if (TextUtils.isEmpty(binding.etPassword.text.toString())) {
            isValid = false
            binding.etPassword.error = "Required Field"
        }

        if (!binding.tvTerms.isChecked) {
            toast("Please select terms and conditions")
        }
        if (TextUtils.isEmpty(binding.etPassword.text.toString())) {
            isValid = false
            binding.etPassword.error = "Required Field"
        } else if (binding.etPassword.text!!.length.toString() < "6") {
            binding.etPassword.error = "Enter a password more than 6 letters"
        }
        return isValid
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.btnLogin -> {
//               startActivity(Intent(this,DetailActivity::class.java))
                if (validation()) {
                    signUpApi()
                }
            }

        }
    }

    private fun signUpApi() {
//        Utility.getDeviceToken()
        val user = binding.etUser.text.toString()
        val name = binding.etName.text.toString()
        val mobile = binding.etMobile.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val deviceToken = "1234@1234"
        val body = SignUpPramModel(
            deviceToken.toString(),
            deviceType = "android",
            email,
            name,
            password,
            mobile,
            user
        )
        viewModel.onSignUp(body)
    }
}