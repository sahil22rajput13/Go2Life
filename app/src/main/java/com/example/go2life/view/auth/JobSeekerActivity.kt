package com.example.go2life.view.auth

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.go2life.base.BaseActivity
import com.example.go2life.base.MyApplication
import com.example.go2life.databinding.ActivityJobSeekerBinding
import com.example.go2life.model.profile.ProfilePramModel
import com.example.go2life.utils.CameraPermissionHandler
import com.example.go2life.utils.Status.*
import com.example.go2life.utils.openGalleryPicker
import com.example.go2life.utils.showCameraPermissionDialog
import com.example.go2life.utils.takePhotoWithCamera
import com.example.go2life.utils.toast
import com.example.go2life.view.navigation.MainActivity
import com.example.go2life.viewmodels.AuthViewModel
import com.example.go2life.viewmodels.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class JobSeekerActivity : BaseActivity(), View.OnClickListener {
    private val CAMERA_PERMISSION_REQUEST = 101
    private val GALLERY_PERMISSION_REQUEST = 102

    private lateinit var binding: ActivityJobSeekerBinding
    private var city: String = ""
    private var country: String = ""
    private var code: String = ""
    private val viewModel by viewModels<AuthViewModel> { ViewModelFactory(application, repository) }
    private lateinit var currentPhotoPath: String
    private val cameraPermissionHandler = CameraPermissionHandler()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityJobSeekerBinding.inflate(layoutInflater)
        binding.onClick = this
        initData()
        observer()
        selfCameraCheck()

        setContentView(binding.root)
    }

    private fun selfCameraCheck() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST
            )
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                GALLERY_PERMISSION_REQUEST
            )
        }
    }

    private fun initData() {
        city = intent.getStringExtra("City").toString()
        country = intent.getStringExtra("Country").toString()
        code = intent.getStringExtra("Code").toString()
    }

    private fun observer() {
        viewModel.resultProfile.observe(this) { data ->
            when (data.status) {
                SUCCESS -> {
                    MyApplication.hideLoader()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }

                LOADING -> MyApplication.showLoader(this)

                ERROR -> {
                    MyApplication.hideLoader()
                    toast(data.message.toString())
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnAddPhoto -> {
                if (hasCameraPermission()) {
                    showImagePickerDialog()
                } else {
                    openCamera()
                }
            }

            binding.btnLogin -> {
                city = intent.getStringExtra("City").toString()
                country = intent.getStringExtra("Country").toString()
                code = intent.getStringExtra("Code").toString()
                val profilePic = binding.tvForgot.toString()
                val jobTitle = binding.etName.text.toString()
                val isStudent = binding.etStudent.text.toString()
                val body = ProfilePramModel(
                    city,
                    company_title = "null",
                    country,
                    country,
                    isStudent,
                    jobTitle,
                    code,
                    profilePic,
                    profile_type = "0",
                    town = "null"
                )
                viewModel.onProfile(body)
            }

            binding.ivLogin -> onBackPressed()
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

    private fun openCamera() {
        if (!hasCameraPermission()) {
            showCameraPermissionDialog(this.cameraPermissionHandler)
            return
        }
        val photoFile = createImageFile()
        takePhotoWithCamera(photoFile, REQUEST_IMAGE_CAPTURE)
    }

    private fun openGallery() {
        openGalleryPicker(REQUEST_GALLERY_IMAGE)
    }

    private fun createImageFile(): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private suspend fun Activity.handleActivityResultWithCoroutines(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        currentPhotoPath: String,
        binding: ActivityJobSeekerBinding
    ) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_GALLERY_IMAGE -> {
                    val selectedImageUri: Uri? = data?.data
                    selectedImageUri?.let {
                        val bitmap: Bitmap? = withContext(Dispatchers.IO) {
                            MediaStore.Images.Media.getBitmap(contentResolver, it)
                        }
                        bitmap?.let {
                            binding.tvForgot.setImageBitmap(it)
                        }
                    }
                }

                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap: Bitmap? = withContext(Dispatchers.IO) {
                        BitmapFactory.decodeFile(currentPhotoPath)
                    }
                    imageBitmap?.let {
                        binding.tvForgot.setImageBitmap(it)
                    }
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        CoroutineScope(Dispatchers.Main).launch {
            handleActivityResultWithCoroutines(
                requestCode, resultCode, data,
                currentPhotoPath, binding
            )
        }
    }

    companion object {
        private const val REQUEST_GALLERY_IMAGE = 100
        private const val REQUEST_IMAGE_CAPTURE = 200
    }

}
