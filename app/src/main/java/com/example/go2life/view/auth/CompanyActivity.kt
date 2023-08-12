package com.example.go2life.view.auth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.go2life.viewmodels.ViewModelFactory
import com.example.go2life.base.BaseActivity
import com.example.go2life.base.MyApplication
import com.example.go2life.databinding.ActivityCompanyBinding
import com.example.go2life.model.profile.ProfilePramModel
import com.example.go2life.utils.CameraPermissionHandler
import com.example.go2life.utils.Status
import com.example.go2life.utils.toast
import com.example.go2life.view.navigation.MainActivity
import com.example.go2life.viewmodels.AuthViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class CompanyActivity : BaseActivity(), View.OnClickListener {
    val viewModel by viewModels<AuthViewModel> { ViewModelFactory(application, repository) }
    lateinit var binding: ActivityCompanyBinding
    private lateinit var currentPhotoPath: String
    private val cameraPermissionHandler = CameraPermissionHandler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompanyBinding.inflate(layoutInflater)
        binding.onClick = this
        observer()
        bundleData()
        setContentView(binding.root)
    }

    private fun bundleData() {

    }

    private fun validation(): Boolean {
        var isValid = true
        if (TextUtils.isEmpty(binding.etStudent.text.toString())) {
            isValid = false
            binding.etStudent.error = "Required Field"
        }
        return isValid
    }

    private fun observer() {
        viewModel.resultProfile.observe(this) { it ->
            it.let { data ->
                when (data.status) {
                    Status.SUCCESS -> {
                        MyApplication.hideLoader()
                        val company_title = binding.etStudent.text.toString()
                        val profile_type = "Company"
                        val profile_pic = binding.tvForgot.toString()
                        val bundle = Bundle().apply {
                            putString("profile_type", profile_type)
                            putString("profile_pic", profile_pic)
                            putString("company_title", company_title)
                        }
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtras(bundle)
                        startActivity(intent)
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

    override fun onClick(p0: View?) {
        when (p0) {
            binding.btnLogin -> {
                if (validation()) {
                    val city = intent.getStringExtra("selectedCity").toString()
                    val company_Tittle = binding.etStudent.text.toString()
                    val countryName = intent.getStringExtra("selectedCountry").toString()
                    val is_student = intent.getStringExtra("is_student").toString()
                    val job_title = intent.getStringExtra("job_title").toString()
                    val post_code = intent.getStringExtra("post_code").toString()
                    val profile_pic = intent.getStringExtra("profile_pic").toString()
                    val body = ProfilePramModel(
                        city,
                        company_Tittle,
                        country = "null",
                        countryName,
                        is_student,
                        job_title,
                        post_code,
                        profile_pic,
                        profile_type = "1",
                        town = "null"
                    )
                    viewModel.onProfile(body)
                }
            }


            binding.ivLogin -> {
                onBackPressed()
            }

            binding.btnAddPhoto -> {

                if (hasCameraPermission()) {
                    // User has the camera permission, proceed with opening camera or gallery
                    showImagePickerDialog()
                } else {
                    // Camera permission is not granted, open app settings
                    cameraPermissionHandler.openAppSettings(this)
                }
            }
        }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun showImagePickerDialog() {
        val items = arrayOf("Take photo", "Choose from Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setItems(items) { dialog, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
            dialog.dismiss()
        }
        builder.show()
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun openCamera() {


        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.resolveActivity(packageManager)?.let {
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                // Handle file creation error
                null
            }
            photoFile?.let {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    "com.example.go2life.fileprovider",
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_GALLERY_IMAGE)
    }

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            // Save a file path for use with ACTION_VIEW intents

            currentPhotoPath = absolutePath
        }
    }

    companion object {
        private const val REQUEST_GALLERY_IMAGE = 100
        private const val REQUEST_IMAGE_CAPTURE = 200
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_GALLERY_IMAGE -> {
                    val selectedImageUri: Uri? = data?.data
                    selectedImageUri?.let {
                        val bitmap: Bitmap? = MediaStore.Images.Media.getBitmap(
                            this.contentResolver,
                            it
                        )
                        binding.tvForgot.setImageBitmap(bitmap)
                    }
                }

                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap: Bitmap? = BitmapFactory.decodeFile(currentPhotoPath)
                    imageBitmap?.let {
                        binding.tvForgot.setImageBitmap(it)
                    }
                }
            }
        }
    }
}