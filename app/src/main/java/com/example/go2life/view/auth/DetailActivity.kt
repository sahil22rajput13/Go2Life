package com.example.go2life.view.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.viewModels
import com.example.go2life.viewmodels.ViewModelFactory
import com.example.go2life.R
import com.example.go2life.base.BaseActivity
import com.example.go2life.base.GetObjects
import com.example.go2life.base.MyApplication
import com.example.go2life.databinding.ActivityDetailBinding
import com.example.go2life.model.city.CityPramModel
import com.example.go2life.utils.SharedPreference
import com.example.go2life.utils.Status.ERROR
import com.example.go2life.utils.Status.LOADING
import com.example.go2life.utils.Status.SUCCESS
import com.example.go2life.utils.toast
import com.example.go2life.viewmodels.AuthViewModel

class DetailActivity : BaseActivity(), View.OnClickListener {
    private val viewModel by viewModels<AuthViewModel> { ViewModelFactory(application, repository) }
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    lateinit var binding: ActivityDetailBinding
    val mList = ArrayList<String>()
    val mCity = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        binding.onClick = this
        spinnerData()
        countryApi()
        observer()
        setContentView(binding.root)


    }


    private fun observer() {
        viewModel.resultCity.observe(this) { cityDataResult ->
            cityDataResult?.let { cityData ->
                when (cityData.status) {
                    SUCCESS -> {
                        MyApplication.hideLoader()
                        mCity.clear()
                        GetObjects.preference.putString(SharedPreference.Key.POSTCODE, "2213")
                        val response = cityData.data!!.body
                        for (data in cityData.data.body) {
                            mCity.add(data.city_name)
                        }

//                        fun findCountryId(selectedCity: String): String {
//                            for (detailData in response) {
//                                if (detailData.city_name == selectedCity) {
//                                    return detailData.city_id.toString()
//                                }
//                            }
//                            return null.toString()
//                        }
//                        autoCompleteTextView = binding.tvName.editText as AutoCompleteTextView
//                        autoCompleteTextView.onItemClickListener =
//                            AdapterView.OnItemClickListener { parent, _, position, _ ->
//                                val selectedCity =
//                                    parent.getItemAtPosition(position)
//                                val selectedCityId =
//                                    findCountryId(selectedCity as String)
//                                if (selectedCityId == binding.etEmail.text.toString()) {
//                                    startActivity(Intent(this, JobSeekerActivity::class.java))
//                                }else if (TextUtils.isEmpty(binding.etEmail.text.toString())){
//                                    binding.etEmail.error = "Required Field"
//                                }else{
//                                    toast("PostCode doesn't Match")
//                                }
//                            }
                    }

                    LOADING -> {
                        MyApplication.showLoader(this)
                    }

                    ERROR -> {
                        MyApplication.hideLoader()
                        toast(cityData.message.toString())
                    }
                }
            }
        }
        viewModel.resultCountry.observe(this) { dataResult ->
            dataResult?.let { data ->
                when (data.status) {
                    SUCCESS -> {
                        MyApplication.hideLoader()
                        val response = data.data?.body
                        if (response != null) {
                            mList.clear()
                            fun findCountryId(selectedCountry: String): String {
                                for (detailData in response) {
                                    if (detailData.country_name == selectedCountry) {
                                        return detailData.country_id.toString()
                                    }
                                }
                                return null.toString()
                            }

                            for (countryData in response) {
                                mList.add(countryData.country_name)
                            }

                            autoCompleteTextView = binding.tvName.editText as AutoCompleteTextView
                            autoCompleteTextView.onItemClickListener =
                                AdapterView.OnItemClickListener { parent, _, position, _ ->
                                    val selectedCountry =
                                        parent.getItemAtPosition(position)
                                    val selectedCountryId =
                                        findCountryId(selectedCountry as String)
                                    val body = CityPramModel(selectedCountryId)
                                    viewModel.onCity(body)
                                }
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


    private fun spinnerData() {
        autoCompleteTextView = binding.tvName.editText as AutoCompleteTextView
        autoCompleteTextView.setAdapter(
            ArrayAdapter(
                this,
                R.layout.spinner_list,
                mList
            )
        )
        autoCompleteTextView = binding.tvPassword.editText as AutoCompleteTextView
        autoCompleteTextView.setAdapter(
            ArrayAdapter(
                this,
                R.layout.spinner_list,
                mCity
            )
        )
    }


    private fun validation(): Boolean {
        var isValid = true
//        if (TextUtils.isEmpty(binding.mySpinnerDropdown.text.toString())) {
//            isValid = false
//            binding.mySpinnerDropdown.error = "Required Field"
//        }
        if (TextUtils.isEmpty(binding.etEmail.text.toString())) {
            isValid = false
            binding.etEmail.error = "Required Field"
        }


//        if (TextUtils.isEmpty(binding.etPassword.text.toString())) {
//            isValid = false
//            binding.etPassword.error = "Required Field"
//        }
        return isValid
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.btnLogin -> {
                if (validation()) {
                    val selectedCity = binding.tvPassword.editText?.text.toString()
                    val selectedCountry = binding.tvName.editText?.text.toString()
                    val postCode = binding.etEmail.editableText.toString()
                    val intent = Intent(this, SeekingActivity::class.java)
                    intent.putExtra("City", selectedCity)
                    intent.putExtra("Code", postCode)
                    intent.putExtra("Country", selectedCountry)
                    startActivity(intent)
                }
            }
        binding.ivLogin -> {
            onBackPressed()
        }
    }

}

private fun countryApi() {
    viewModel.onCountry()
}

}




